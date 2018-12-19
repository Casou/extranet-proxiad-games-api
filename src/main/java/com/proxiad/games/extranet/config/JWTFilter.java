package com.proxiad.games.extranet.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.proxiad.games.extranet.annotation.AdminTokenSecurity;
import com.proxiad.games.extranet.annotation.BypassSecurity;
import com.proxiad.games.extranet.model.Token;
import com.proxiad.games.extranet.repository.TokenRepository;
import com.proxiad.games.extranet.utils.URIPatternMatchers;

@Configuration
public class JWTFilter extends GenericFilterBean {

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	@Value("${extranet.admin.token}")
	private String adminToken;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String token = request.getHeader("Authorization");

		HandlerMethod handlerMethod = handlerMapping.getHandlerMethods().entrySet().stream()
				.filter(entry -> URIPatternMatchers.matches(request.getRequestURI(), entry.getKey().getPatternsCondition().getPatterns().iterator().next()))
				.collect(Collectors.toList())
				.get(0).getValue();

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.sendError(HttpServletResponse.SC_OK, "success");
			return;
		}

		if (allowRequestWithoutToken(request, handlerMethod)) {
			response.setStatus(HttpServletResponse.SC_OK);
			filterChain.doFilter(req, res);
			return;
		}

		if (token == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		if (allowRequestWithSpecialToken(handlerMethod, token)) {
			response.setStatus(HttpServletResponse.SC_OK);
			filterChain.doFilter(req, res);
			return;
		}

		Optional<Token> optionalToken = this.tokenRepository.findById(token);
		if (!optionalToken.isPresent() || !isTokenValid(optionalToken.get())) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		Token tokenEntity = optionalToken.get();
		tokenEntity.setExpirationDate(LocalDateTime.now().plusHours(1));
		tokenRepository.save(tokenEntity);

//				ObjectId userId = new ObjectId(tokenService.getUserIdFromToken(token));
//				request.setAttribute("userId", "test");

		filterChain.doFilter(req, res);
	}

	private boolean allowRequestWithSpecialToken(HandlerMethod handlerMethod, String tokenString) {
		if (handlerMethod.getMethodAnnotation(AdminTokenSecurity.class) != null) {
			return tokenString.equals(adminToken);
		}

		return false;
	}

	private boolean isTokenValid(Token token) {
		return token.getExpirationDate().isAfter(LocalDateTime.now());
	}

	private boolean allowRequestWithoutToken(HttpServletRequest request, HandlerMethod handlerMethod) {
		return request.getRequestURI().contains("/console") || handlerMethod.getMethodAnnotation(BypassSecurity.class) != null;
	}

}
