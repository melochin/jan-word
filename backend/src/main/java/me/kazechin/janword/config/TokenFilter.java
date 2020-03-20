package me.kazechin.janword.config;


import me.kazechin.janword.user.TokenService;
import me.kazechin.janword.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class TokenFilter  extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

		String token = getToken(httpServletRequest);
		if (token == null) {
			SecurityContextHolder.clearContext();
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		try {
			Authentication authentication = getAuthentication(token);
			if (authentication != null) {
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(authentication);
				SecurityContextHolder.setContext(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccessDeniedException("token过期");
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	public String getToken(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getHeader("token");
	}

	public Authentication getAuthentication(String token) {

		UserInfo user = tokenService.decode(token);
		if (user == null) {
			throw new AccessDeniedException("用户不存在");
		}

		return new UsernamePasswordAuthenticationToken(
				user, null, user.getAuthorities());
	}


}
