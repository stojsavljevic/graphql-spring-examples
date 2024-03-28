package com.alex.graphql.kickstart.listeners;

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

@Component
class SubscriptionsAuthenticationConnectionListener implements ApolloSubscriptionConnectionListener  {

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
		} 
	}
	
}