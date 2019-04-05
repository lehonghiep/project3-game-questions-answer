package com.hust.project3.gamequestionsanswer.service

import com.hust.project3.gamequestionsanswer.database.model.GameRoom
import java.util.*

interface GamePlayService {
    fun checkRoomAvailable(): Boolean

    fun createNewRoom(accountId: String)

    fun matchPlayerByElo(accountId: String, rangeOfElo: Int)
    //    fun sendTo(path: String, result: Any)
    fun leaveRoom(accountId: String)

    fun readyPlay(accountId: String, roomId: String?)

    fun getListRoom(): HashMap<UUID, GameRoom>

    fun invitePlayerByPhoneNumber(accountIdPlayerSendInvite: String, phoneNumber: String)

    fun handleRoomContainPlayer(accountId: String): Boolean
}