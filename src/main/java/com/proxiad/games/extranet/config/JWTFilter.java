package com.proxiad.games.extranet.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
public class JWTFilter extends GenericFilterBean {

//	private TokenService tokenService;

	JWTFilter() {
//		this.tokenService = new TokenService();
	}

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
		} else {
			if (token == null || !isTokenValid(token)) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
//				ObjectId userId = new ObjectId(tokenService.getUserIdFromToken(token));
				request.setAttribute("userId", "test");
				filterChain.doFilter(req, res);
			}
		}
	}

	public boolean isTokenValid(String token) {
		// TODO Check en base de données
		return token != null;
	}

	public boolean allowRequestWithoutToken(HttpServletRequest request) {
		return request.getRequestURI().contains("/login");
	}

}
