package com.hust.project3.gamequestionsanswer.controller

import com.elomath.pro.constant.EloConstants
import com.elomath.pro.constant.Key
import com.elomath.pro.log.LogFactory
import com.elomath.pro.service.GamePlayService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("/player")
class GamePlayController {
    @Autowired
    private lateinit var gamePlayService: GamePlayService

    @MessageMapping("/searching")
    @Throws(Exception::class)
    fun searching(@Header(value = Key.HEADER_ACCOUNT_ID) accountId: String) {
        LogFactory.logger.info("GamePlayController searching Account Id: $accountId")
        if (!gamePlayService.handleRoomContainPlayer(accountId)) {
            if (!gamePlayService.checkRoomAvailable()) {
                gamePlayService.createNewRoom(accountId)
            } else {
                gamePlayService.matchPlayerByElo(accountId, EloConstants.RANGE_ELO_MATCH)
            }
        }
    }

    @MessageMapping("/leave-room")
    @Throws(Exception::class)
    fun leaveRoom(@Header(value = Key.HEADER_ACCOUNT_ID) accountId: String) {
        LogFactory.logger.info("GamePlayController leaveRoom Account Id: $accountId")
        gamePlayService.leaveRoom(accountId)
    }

    @MessageMapping("/ready")
    @Throws(Exception::class)
    fun ready(@Header(value = Key.HEADER_ACCOUNT_ID) accountId: String,
              @Header(value = Key.HEADER_GAMEROOM_ID) gameRoomId: String) {
        LogFactory.logger.info("GamePlayController ready Account Id: $accountId")
        gamePlayService.readyPlay(accountId, gameRoomId)
    }

    @MessageMapping("/invite-player")
    @Throws(Exception::class)
    fun invitePlayer(@Header(value = Key.HEADER_ACCOUNT_ID) accountIdPlayerSendInvite: String,
                     @Header(value = Key.HEADER_PHONE_NUMBER) phoneNumber: String) {
        gamePlayService.invitePlayerByPhoneNumber(accountIdPlayerSendInvite, phoneNumber)
    }

}