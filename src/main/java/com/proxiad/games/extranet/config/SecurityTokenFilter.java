package com.proxiad.games.extranet.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.dto.TokenDto;
import com.proxiad.games.extranet.repository.RoomRepository;
import com.proxiad.games.extranet.service.AuthService;
import com.proxiad.games.extranet.utils.URIPatternMatchers;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityTokenFilter extends GenericFilterBean {

    @Autowired
    private AuthService authService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    @Qualifier("handlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @Value("${extranet.admin.token}")
    private String adminToken;

    @Value("${logging.custom.request.level}")
    private CustomLogLevel requestLogLevel = CustomLogLevel.NONE;
    @Value("${logging.custom.response.level}")
    private CustomLogLevel responseLogLevel = CustomLogLevel.NONE;

    @Bean
    @Primary
    public RequestMappingHandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader("Authorization");
        if (token == null && request.getParameter("token") != null) {
            token = request.getParameter("token");
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_OK, "success");
            return;
        }

        final String uri = request.getRequestURI();

        List<Map.Entry<RequestMappingInfo, HandlerMethod>> filteredMethods = handlerMapping.getHandlerMethods().entrySet().stream()
                .filter(entry -> URIPatternMatchers.matches(uri, entry.getKey().getPatternsCondition().getPatterns().iterator().next()))
                .filter(entry -> entry.getKey().getMethodsCondition().getMethods().stream().anyMatch(methodType -> methodType.name().equals(request.getMethod())))
                .collect(Collectors.toList());
        HandlerMethod handlerMethod = filteredMethods.size() > 0 ? filteredMethods.get(0).getValue() : null;

        CustomLogRequestWrapper requestWrapper = new CustomLogRequestWrapper(request);
        CustomLogResponseWrapper responseWrapper = new CustomLogResponseWrapper(response);

        logRequest(requestWrapper);

        if (allowRequestWithoutToken(request, handlerMethod)) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(requestWrapper, responseWrapper);
            logResponse(responseWrapper, requestWrapper);
            return;
        }

        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            logResponse(responseWrapper, requestWrapper, "token is null");
            return;
        }

        if (allowRequestWithSpecialToken(handlerMethod, token)) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(requestWrapper, responseWrapper);
            logResponse(responseWrapper, requestWrapper);
            return;
        }

        Optional<TokenDto> optionalToken = this.authService.validateToken(token);
        if (!optionalToken.isPresent()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            logResponse(responseWrapper, requestWrapper, String.format("token %s is not valid", token));
            return;
        }

        request.setAttribute("token", token);
        request.setAttribute("room", roomRepository.findByToken(token));

        filterChain.doFilter(requestWrapper, responseWrapper);
        logResponse(responseWrapper, requestWrapper);
    }

    private boolean allowRequestWithSpecialToken(HandlerMethod handlerMethod, String tokenString) {
        if (handlerMethod != null && handlerMethod.getMethodAnnotation(AdminTokenSecurity.class) != null) {
            return tokenString.equals(adminToken);
        }

        return false;
    }

    private boolean allowRequestWithoutToken(HttpServletRequest request, HandlerMethod handlerMethod) {
        return request.getRequestURI().contains("/console")
                || request.getRequestURI().contains("/ws")
                || request.getRequestURI().contains("/actuator")
                || request.getRequestURI().contains("/favicon.ico")
                || (handlerMethod != null && handlerMethod.getMethodAnnotation(BypassSecurity.class) != null);
    }

    private void logRequest(CustomLogRequestWrapper requestWrapper) {
        if (requestLogLevel == CustomLogLevel.NONE) {
            return;
        }

        if (requestLogLevel == CustomLogLevel.BASIC) {
            log.debug("REST Request : " + requestWrapper.getRequestURI());
            return;
        }

        StringBuilder headersString = new StringBuilder();
        Enumeration<String> e = requestWrapper.getHeaderNames();
        while (e.hasMoreElements()) {
            String headers = e.nextElement();
            if (headers != null) {
                headersString.append(headers).append(" :: ").append(requestWrapper.getHeader(headers)).append(" , ");
            }
        }

        log.debug("\n -----------------REST Request Detail-------------------------"
                + " \n RequestURI :: " + requestWrapper.getMethod() + " " + requestWrapper.getRequestURI()
                + " \n REMOTE ADDRESS :: " + requestWrapper.getRemoteAddr()
                + " \n HEADERS :: [ " + headersString + " ] "
                + " \n REQUEST BODY Size :: " + requestWrapper.payload.length() + " bytes"
                + " \n REQUEST BODY :: " + requestWrapper.payload
                + " \n ContentType :: " + requestWrapper.getContentType());
    }

    private void logResponse(CustomLogResponseWrapper responseWrapper, CustomLogRequestWrapper requestWrapper)
            throws IOException {
        logResponse(responseWrapper, requestWrapper, "");
    }

    private void logResponse(CustomLogResponseWrapper responseWrapper, CustomLogRequestWrapper requestWrapper, String customText)
            throws IOException {
        if (requestLogLevel == CustomLogLevel.NONE) {
            return;
        }

        String customTextFormatted = StringUtils.isEmpty(customText) ? "" : String.format(" (%s)", customText);
        if (requestLogLevel == CustomLogLevel.BASIC) {
            log.debug(String.format("REST Response : %s - %s%s", requestWrapper.getRequestURI(),
                    HttpStatus.valueOf(responseWrapper.getStatus()), customTextFormatted));
            return;
        }

        log.debug("\n -----------------REST Response Detail-------------------------"
                + " \n URI :: " + requestWrapper.getRequestURI()
                + " \n Status :: " + HttpStatus.valueOf(responseWrapper.getStatus()) + customTextFormatted
                + " \n Response BODY Size :: " + responseWrapper.getContent().length() + " bytes"
                + " \n Response BODY :: " + responseWrapper.getContent()
                + " \n Content Type :: " + responseWrapper.getContentType());
    }

}
