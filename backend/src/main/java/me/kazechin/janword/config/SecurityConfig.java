package me.kazechin.janword.config;

import me.kazechin.janword.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	TokenFilter tokenFilter;

	@Bean
	public FilterRegistrationBean registration(@Qualifier("tokenFilter") Filter filter) {
		// 禁止 servlet filter 注册 tokenFilter
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.requestMatcher(request -> match(request))
				.authorizeRequests()
				.anyRequest()
				.hasRole("ADMIN")
				.and()
				.addFilterAfter(tokenFilter, ExceptionTranslationFilter.class)
				.cors()
				.and()
				.csrf()
				.disable();
	}

	private boolean match(HttpServletRequest request) {
		String[] strs = new String[]{
				"/grammars",
				"/grammar",
				"/grammar/**",
				"/words",
				"/word",
				"/word/**"
		};

		for(String uri : strs) {
			if (request.getRequestURL().indexOf(uri) >= 0) return true;
		}

		return false;
	}

}
