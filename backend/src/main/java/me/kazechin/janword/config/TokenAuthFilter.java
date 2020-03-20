package me.kazechin.janword.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jdk.nashorn.internal.parser.Token;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthFilter extends AbstractAuthenticationProcessingFilter{

	/**
	 * url设置的意义在哪里？
	 * @param defaultFilterProcessesUrl
	 */
	protected TokenAuthFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	protected TokenAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		// get token
		String token = request.getHeader("token");

		// decode token
		String username = JWT.require(Algorithm.HMAC256("test")).build().verify(token)
				.getClaim("username").asString();

		// generate authentication

		System.out.println(username);
		return new UsernamePasswordAuthenticationToken(username, username);
	}
}
