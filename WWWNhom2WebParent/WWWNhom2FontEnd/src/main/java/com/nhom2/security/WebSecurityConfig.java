package com.nhom2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerDetailService();
	};

	@Bean
	public PasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(PasswordEncoder());
		return authenticationProvider;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	private static final String[] PUBLIC_MATCHERS = {
			"/images/**", "/js/**", "/webjars/**", "/css/**", "/slick/**", "/style.css", "/fonts/**", "/fontawesome/**",
			"/", "/welcome", "/contact", "/coming-soon", "/shop-grid/**",
			"/login", "/signup", "/register", "/verify/**", "/logout", "/customers/**", "/forgot-password", "/reset-password",
			"/c/**", "/p/**", 
			"/brands-images/**", "/categories-images/**", "/products-images/**" 
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll()
		.anyRequest().authenticated();
		
		http.csrf().disable();
		
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests()
			.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.usernameParameter("email")
				.passwordParameter("password")	
			.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.deleteCookies("JSESSIONID")
			.and()
				.rememberMe()
					.key("nhom2www2021-2022")
					.tokenValiditySeconds(3*24*60*60);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**", "/css/**", "/slick/**", "/fonts/**", "/fontawesome/**");
	}

}
