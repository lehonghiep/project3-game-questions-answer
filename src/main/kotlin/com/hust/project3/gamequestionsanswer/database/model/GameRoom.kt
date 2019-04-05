package com.hust.project3.gamequestionsanswer.database.model

import com.hust.project3.gamequestionsanswer.dto.PlayerInfoDto
import java.util.*

class GameRoom(var gameRoomId: UUID,
               var listPlayerInfo: MutableList<PlayerInfoDto>,
               var status: Boolean,
               var timeStamp: String) {
    //status = true: waiting room
    //status = false: playing room
}