package com.alex.graphql.core.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Profile("!no-security")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers(new AntPathRequestMatcher("/subscriptions"), new AntPathRequestMatcher("/manifest.json")).permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(withDefaults())
			.cors(cors -> cors.disable())
			.csrf(csrf -> csrf.disable());
		
		return http.build();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	UserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
			.username("admin")
			.password("admin")
			.roles("USER")
			.build();

		return new InMemoryUserDetailsManager(user);
	}
	
	// This configuration is needed for security propagation for @Async methods
	// tested with @RestController in Kickstart

//	@Bean(name = "threadPoolTaskExecutor")
//  Executor threadPoolTaskExecutor() {
//      return new ThreadPoolTaskExecutor();
//  }
//	
//	@Bean 
//	DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) { 
//	    return new DelegatingSecurityContextAsyncTaskExecutor(delegate); 
//	}
	// -------- END -----------
	
	// Kickstart 14.1.0 with SB 2.7.8 needs following configuration to properly propagate 
	// security context for subscriptions when calling GraphQLResolver to resolve some field. 
	
	// Kickstart 15.0.0 with SB 3.0.7 doesn't handle subscription security context at all with following configuration.
	// W/o this configuration security context for subscriptions is not properly propagated 
	// when calling GraphQLResolver to resolve some field.
	// IIRC, same goes for DGS
	
//	@Bean 
//	DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) { 
//	    return new DelegatingSecurityContextAsyncTaskExecutor(delegate); 
//	}
//	
//	@Bean("threadPoolTaskExecutor")
//	ThreadPoolTaskExecutor getAsyncExecutor() {
//	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//	    executor.setCorePoolSize(20);
//	    executor.setMaxPoolSize(1000);
//	    executor.setQueueCapacity(11);
//	    executor.setWaitForTasksToCompleteOnShutdown(true);
//	    executor.setThreadNamePrefix("AsyncExecutor-");
//	    return executor;
//	}
//	
//	@Bean
//	MethodInvokingFactoryBean methodInvokingFactoryBean() {
//	    MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
//	    methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
//	    methodInvokingFactoryBean.setTargetMethod("setStrategyName");
//	    methodInvokingFactoryBean.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//	    return methodInvokingFactoryBean;
//	}
	
	// -------- END -----------
	
}

