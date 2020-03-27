package me.kazechin.janword.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		//TODO 普通用户权限有问题
		http.requestMatcher(request -> match(request))
				.authorizeRequests()
				.mvcMatchers(adminURL())
				.hasRole("ADMIN")
				.mvcMatchers(userURL())
				.hasRole("USER")
				.and()
				.addFilterAfter(tokenFilter, ExceptionTranslationFilter.class)
				.cors()
				.and()
				.csrf()
				.disable();
	}

	private String[] userURL() {
		return new String[]{
				"/card/words",
				"/card/word/finish",
				"/card/word/remeber/",
				"/card/word/forget/",
				"/card/grammars",
				"/card/grammar/finish",
				"/card/grammar/remeber",
				"/card/grammar/forget",
				"/memory/record",
				"/memory/record/count"
		};
	}

	private String[] adminURL() {
		return new String[] {
				"/words",
				"/word",
				"/grammars",
				"/grammar"
		};
	}

	private boolean match(HttpServletRequest request) {

		List<String> strs = new ArrayList<>();
		strs.addAll(Arrays.asList(adminURL()));
		strs.addAll(Arrays.asList(userURL()));

		for (String uri : strs) {
			if (request.getRequestURI().indexOf(uri) == 0) return true;
		}

		return false;
	}

}
