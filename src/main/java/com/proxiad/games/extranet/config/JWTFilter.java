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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import com.proxiad.games.extranet.model.Token;
import com.proxiad.games.extranet.repository.TokenRepository;

@Configuration
public class JWTFilter extends GenericFilterBean {

	@Autowired
	private TokenRepository tokenRepository;

	@Value("${extranet.admin.token}")
	private String adminToken;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String token = request.getHeader("Authorization");

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.sendError(HttpServletResponse.SC_OK, "success");
			return;
		}

		if (allowRequestWithoutToken(request)) {
			response.setStatus(HttpServletResponse.SC_OK);
			filterChain.doFilter(req, res);
			return;
		}

		if (token == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		if (allowRequestWithSpecialToken(request, token)) {
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

	public boolean allowRequestWithSpecialToken(HttpServletRequest request, String tokenString) {
		if (request.getRequestURI().startsWith("/admin")) {
			return tokenString.equals(adminToken);
		}

		return false;
	}

	private boolean isTokenValid(Token token) {
		return token.getExpirationDate().isAfter(LocalDateTime.now());
	}

	public boolean allowRequestWithoutToken(HttpServletRequest request) {
		return request.getRequestURI().contains("/login") || request.getRequestURI().contains("/console");
	}

}
