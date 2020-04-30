package com.morissoft.printing.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableJpaRepositories("com.morissoft.printing.repository")
@ComponentScan(basePackages = {"com.morissoft.printing"})
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource datasource;
	
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/bootstrap/**", "/bootstrap4/**","/dist/**", "/js/**", "/plugins/**", "/images/**").permitAll()
				.antMatchers("/items/**","/users/**","/customers/**","/reports/**","/payments/void.html/**").access("hasRole('ROLE_ADMIN')")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.failureUrl("/login?error")
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login")
				.permitAll()
				.and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
			
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Use Spring Boots User detailsMAnager
		JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();

		// Set our Datasource to use the one defined in application.properties
		userDetailsService.setDataSource(datasource);

		// Create BCryptPassword encoder
		PasswordEncoder encoder = new BCryptPasswordEncoder();

		// add components
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
		auth.jdbcAuthentication().dataSource(datasource);

		// add new user "user" with password "password" - password will be encrypted
		if (!userDetailsService.userExists("naruto")) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_OPERATOR"));
			User userDetails = new User("naruto", encoder.encode("1234"), authorities);
			userDetailsService.createUser(userDetails);
		}
		if (!userDetailsService.userExists("unyil")) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));
			User userDetails = new User("unyil", encoder.encode("1234"), authorities);
			userDetailsService.createUser(userDetails);
		}
		if (!userDetailsService.userExists("morissoft")) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			User userDetails = new User("morissoft", encoder.encode("1234"), authorities);
			userDetailsService.createUser(userDetails);
		}
		
	}
//	@Autowired
//    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
//
//    @PostConstruct
//    public void init() {
//       requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
//    }	
}
