package com.hust.project3.gamequestionsanswer.controller

import com.hust.project3.gamequestionsanswer.constant.Key
import com.hust.project3.gamequestionsanswer.dto.AnswerDto
import com.hust.project3.gamequestionsanswer.service.MatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import java.util.*

@Controller
@MessageMapping("/match")
class MatchController {
    @Autowired
    lateinit var matchService: MatchService

    @MessageMapping("/answer")
    @Throws(Exception::class)
    fun receiveAnswer(@Header(value = Key.HEADER_GAMEROOM_ID) gameRoomId: String,
                      @Header(value = Key.HEADER_ACCOUNT_ID) accountId: String,
                      @Payload answerDto: AnswerDto) {
        matchService.receiveAnswer(UUID.fromString(gameRoomId),
                UUID.fromString(accountId), answerDto)
    }

    @MessageMapping("/give-up")
    @Throws(Exception::class)
    fun playerGiveUp(@Header(value = Key.HEADER_GAMEROOM_ID) gameRoomId: String,
                     @Header(value = Key.HEADER_ACCOUNT_ID) accountId: String) {
        matchService.playerGiveUp(UUID.fromString(gameRoomId), UUID.fromString(accountId))
    }

}