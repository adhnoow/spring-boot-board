package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // Spring Security-Filter, FilterChain에 등록하겠다 선언
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)

// WebSecurityConfigurerAdapter: 취사 선택
public class BoardSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/user/**").authenticated() // login만 해도 접근 가능
				.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // admin or
																										// manager 접근 가능
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 최고 관리자인 admin만 접근 가능
				.anyRequest() // 이외의 것들은
				.permitAll() // 나머지가 갈 곳
				.and().formLogin() // login을 할 건데
				.loginPage("/loginForm") // 여기로 와라
				.loginProcessingUrl("/login") // login button 누르면 이동해야 할 경로
				// defaultSuccessUrl: login 성공 후 user 로그인 -> user 로그인 페이지 리턴, admin 로그인 시도 ->
				// admin 페이지 리턴
				.defaultSuccessUrl("/");
//				.failureUrl("/login-fail");

		http.exceptionHandling()
			.accessDeniedPage("/accessDenied");
	}

	// 로그인 상태인 유저에게는 로그아웃 버튼만, 로그아웃 상태인 유저에게는 로그아웃 버튼만 주는 속성을 위해
//	@Bean
//	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver, SpringSecurityDialect sec) {
//		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//		templateEngine.setTemplateResolver(templateResolver);
//		templateEngine.addDialect(sec); // Enable use of "sec"
//		return templateEngine;
//	}
}