package learn.chronicles;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocketMessageBroker
@ConditionalOnWebApplication

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		//topic prefixes
		//note the video uses registry. not config.
		config.enableSimpleBroker("/room");
		config.setApplicationDestinationPrefixes("/app");
		config.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//starts as a starting point for a websocket

		//you can set the cross origins with .setallowedoriginpatterns

		//not sure what sockJS does yet

		//this is the endpoint needed to connect to the SockJS
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
	}

	@Bean
	public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(64000);
		container.setMaxBinaryMessageBufferSize(6400);
		return container;
	}



}
