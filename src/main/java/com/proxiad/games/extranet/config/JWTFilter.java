package com.proxiad.games.extranet.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

@Configuration
@Slf4j
public class JWTFilter extends GenericFilterBean {

    @Autowired
    private AuthService authService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    @Qualifier("handlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @Value("${extranet.admin.token}")
    private String adminToken;

    @Bean
    @Primary
    public RequestMappingHandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
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
                .collect(Collectors.toList());
        HandlerMethod handlerMethod = filteredMethods.size() > 0 ? filteredMethods.get(0).getValue() : null;

        if (allowRequestWithoutToken(request, handlerMethod)) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(req, res);
            return;
        }

        if (token == null) {
            log.warn(String.format("401 Request unauthorized (token is null) for uri : %s", uri));
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (allowRequestWithSpecialToken(handlerMethod, token)) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(req, res);
            return;
        }

        Optional<TokenDto> optionalToken = this.authService.validateToken(token);
        if (!optionalToken.isPresent()) {
            log.warn(String.format("401 Request unauthorized (token is not valid) for token %s and uri : %s", token, uri));
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        request.setAttribute("token", token);
        request.setAttribute("room", roomRepository.findByToken(token));

        filterChain.doFilter(req, res);
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

}
