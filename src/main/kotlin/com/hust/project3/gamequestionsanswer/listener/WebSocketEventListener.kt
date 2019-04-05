package com.hust.project3.gamequestionsanswer.listener

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        logger.info("WebSocket Connected")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        logger.info("WebSocket Disconnected")
    }

}