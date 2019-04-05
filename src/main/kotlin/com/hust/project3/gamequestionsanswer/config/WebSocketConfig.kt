package com.hust.project3.gamequestionsanswer.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    @Value("\${websocket.destination.prefix.broker}")
    private lateinit var simpleBrokerPrefix: String

    @Value("\${websocket.destination.prefix.application}")
    private lateinit var applicationDestinationPrefix: String

    @Value("\${websocket.stomp.endpoint}")
    private lateinit var stompEndpointRegistry: String

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker(simpleBrokerPrefix)
        config.setApplicationDestinationPrefixes(applicationDestinationPrefix)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint(stompEndpointRegistry)
                .setAllowedOrigins("*")
                .withSockJS()
    }

}