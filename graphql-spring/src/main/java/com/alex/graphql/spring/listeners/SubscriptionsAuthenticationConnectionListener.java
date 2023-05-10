package com.alex.graphql.spring.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketSessionInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class SubscriptionsAuthenticationConnectionListener implements WebSocketGraphQlInterceptor {
	
	Logger logger = LoggerFactory.getLogger(SubscriptionsAuthenticationConnectionListener.class);

	@Override
	public Mono<Object> handleConnectionInitialization(
			WebSocketSessionInfo sessionInfo, Map<String, Object> connectionInitPayload) {
		
		String apolloToken = (String) connectionInitPayload.get("authToken");
		
		if (apolloToken != null) {
			logger.info("Authentication token: {}", apolloToken);

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			User user = new User(apolloToken, "password", authorities);
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, "password", authorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		return Mono.empty();
	}
}
