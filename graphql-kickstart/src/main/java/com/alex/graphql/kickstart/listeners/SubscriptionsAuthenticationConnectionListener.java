package com.alex.graphql.kickstart.listeners;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import graphql.kickstart.execution.subscriptions.SubscriptionSession;
import graphql.kickstart.execution.subscriptions.apollo.ApolloSubscriptionConnectionListener;
import graphql.kickstart.execution.subscriptions.apollo.OperationMessage;
import graphql.kickstart.servlet.apollo.ApolloWebSocketSubscriptionSession;
import jakarta.websocket.server.HandshakeRequest;

@Component
class SubscriptionsAuthenticationConnectionListener implements ApolloSubscriptionConnectionListener {

	Logger logger = LoggerFactory.getLogger(SubscriptionsAuthenticationConnectionListener.class);

	@Override
	public void onConnect(SubscriptionSession session, OperationMessage message) {

		@SuppressWarnings("unchecked")
		String apolloToken = ((Map<String, String>) message.getPayload()).get("authToken");

		if (apolloToken != null) {
			logger.info("Authentication token: {}", apolloToken);

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			User user = new User(apolloToken, "password", authorities);
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, "password", authorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else if (SecurityContextHolder.getContext().getAuthentication() == null) {
			
			Principal userPrincipal = getPrincipal(session);
			if(userPrincipal  != null) {
				SecurityContextHolder.getContext().setAuthentication((UsernamePasswordAuthenticationToken) userPrincipal);
			}
		}
	}
	
	Principal getPrincipal(SubscriptionSession session) {
		
		Map<String, Object> userProperties = ((ApolloWebSocketSubscriptionSession) session).getUserProperties();
		HandshakeRequest request = (HandshakeRequest) userProperties
				.get(HandshakeRequest.class.getName());
		return request.getUserPrincipal();
	}
}