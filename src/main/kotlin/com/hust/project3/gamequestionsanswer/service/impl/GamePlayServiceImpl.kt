package com.hust.project3.gamequestionsanswer.service.impl

import com.hust.project3.gamequestionsanswer.constant.MessageKey
import com.hust.project3.gamequestionsanswer.database.model.GameRoom
import com.hust.project3.gamequestionsanswer.dto.PlayerInfoDto
import com.hust.project3.gamequestionsanswer.dto.ResponseDto
import com.hust.project3.gamequestionsanswer.dto.StatusDto
import com.hust.project3.gamequestionsanswer.service.AccountService
import com.hust.project3.gamequestionsanswer.service.GamePlayService
import com.hust.project3.gamequestionsanswer.service.MatchService
import com.hust.project3.gamequestionsanswer.service.UserProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.util.*

@Transactional
@Service
class GamePlayServiceImpl : GamePlayService {
    @Autowired
    private lateinit var simpMessageSendingOperations: SimpMessageSendingOperations

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var userProfileService: UserProfileService

    @Autowired
    private lateinit var matchService: MatchService

    @Value("\${websocket.destination.prefix.broker}")
    private lateinit var simpleBrokerPrefix: String

    private var listRoom = hashMapOf<UUID, GameRoom>()

    override fun checkRoomAvailable(): Boolean {
        //TODO kiem tra xem co phong nao dang o trang thai waiting (room status =true )hay khong
        synchronized(this.listRoom) {
            if (this.listRoom.isEmpty()) {
                return false
            }
            val roomAvailableFilter = this.listRoom.filter { it.value.status }
            if (roomAvailableFilter.isEmpty()) {
                return false
            }
            return true
        }
    }

    override fun createNewRoom(accountId: String) {
        synchronized(this.listRoom) {
            val gameRoomId = UUID.randomUUID()
            val listPlayerInfo = mutableListOf<PlayerInfoDto>()
            val userProfile = userProfileService.getUserProfileByAccountId(accountId)
            val playerInfoDto = PlayerInfoDto(accountId, false, userProfile)
            listPlayerInfo.add(playerInfoDto)
            val gameRoom = GameRoom(gameRoomId, listPlayerInfo, true, getTimeNow())
            this.listRoom[gameRoomId] = gameRoom
            val payload = ResponseDto(gameRoom, StatusDto(MessageKey.SUCCESS))
            sendTo(accountId, payload)
        }
    }

    override fun matchPlayerByPoint(accountId: String, rangeOfPoint: Int) {
        val userProfile = userProfileService.getUserProfileByAccountId(accountId)
        val point = userProfile.point!!
        val roomAvailableFilter = this.listRoom.filter { it.value.status }
        val roomFilterByPoint = roomAvailableFilter.filter {
            Math.abs((it.value.listPlayerInfo[0].userProfileDto.point!!) - point) <= rangeOfPoint
        }
        if (roomFilterByPoint.isEmpty()) {
            createNewRoom(accountId)
        } else {
            synchronized(this.listRoom) {
                val gameRoomId = roomFilterByPoint.keys.elementAt(0)
                val gameRoom = roomFilterByPoint.getValue(gameRoomId)
                val playerInfoDto = PlayerInfoDto(accountId, false, userProfile)
                gameRoom.listPlayerInfo.add(playerInfoDto)
                gameRoom.status = false
                this.listRoom[gameRoomId] = gameRoom
                val payload = ResponseDto(gameRoom, StatusDto(MessageKey.SUCCESS))
                sendTo(gameRoom.listPlayerInfo[0].accountId, payload)
                sendTo(gameRoom.listPlayerInfo[1].accountId, payload)
            }
        }
    }

    private fun getTimeNow() = SimpleDateFormat("HH:mm:ss").format(Date())

    private fun sendTo(path: String, payload: Any) {
        simpMessageSendingOperations.convertAndSend("$simpleBrokerPrefix/$path", payload)
    }

    override fun leaveRoom(accountId: String) {
//        LogFactory.logger.info("before leaveRoom: number rooms = ${listRoom.size}")
        synchronized(this.listRoom) {
            val gameRoom = getRoomContainPlayerById(accountId)

            var payload: ResponseDto<Any>
            if (gameRoom != null) {
                if (gameRoom.listPlayerInfo.size < 2) {
                    this.listRoom.remove(gameRoom.gameRoomId, gameRoom)
                } else {
                    if (gameRoom.listPlayerInfo[0].isReadyPlay && gameRoom.listPlayerInfo[1].isReadyPlay) {
                        sendTo("$accountId/error", ResponseDto(null, StatusDto(MessageKey.PLAYER_DISCONNECT_NETWORK)))
                        return
                    }
                    if (accountId == gameRoom.listPlayerInfo[0].accountId) {
                        this.listRoom.remove(gameRoom.gameRoomId, gameRoom)
                        payload = ResponseDto(null,
                                StatusDto(true,
                                        MessageKey.ROOM_HOST_LEAVE_ROOM_SUCCESS.key,
                                        "timeStamp: " + getTimeNow()))
                        sendTo(accountId, payload)
                        sendTo(gameRoom.listPlayerInfo[1].accountId, payload)
                        return
                    } else {
                        gameRoom.listPlayerInfo.removeAt(1)
                        gameRoom.status = true
                        val playerInfoOfRoomHostNow = gameRoom.listPlayerInfo[0]
                        playerInfoOfRoomHostNow.isReadyPlay = false
                        this.listRoom[gameRoom.gameRoomId] = gameRoom
                        payload = ResponseDto(gameRoom, StatusDto(MessageKey.SUCCESS))
                        sendTo(playerInfoOfRoomHostNow.accountId, payload)
                    }
                }
                payload = ResponseDto(null,
                        StatusDto(true,
                                MessageKey.PLAYER_LEAVE_ROOM_SUCCESS.key,
                                "timeStamp: " + getTimeNow()))
                sendTo(accountId, payload)
            }
        }
    }

    private fun getRoomContainPlayerById(accountId: String): GameRoom? {
        //ToDo tim room co chua accountId
        val gameRoom = this.listRoom.values
                .find {
                    it.listPlayerInfo
                            .find { playerInfoDto -> playerInfoDto.accountId == accountId }?.accountId == accountId
                }
        return gameRoom
    }

    override fun handleRoomContainPlayer(accountId: String): Boolean {
        val gameRoom = getRoomContainPlayerById(accountId)
        if (gameRoom != null) {
            if (gameRoom.status) {
                sendTo(accountId, ResponseDto(gameRoom, StatusDto(MessageKey.SUCCESS)))
                return true
            } else if (!gameRoom.listPlayerInfo[0].isReadyPlay || !gameRoom.listPlayerInfo[1].isReadyPlay) {
                leaveRoom(accountId)
            } else {
                sendTo("$accountId/error", ResponseDto(null, StatusDto(MessageKey.PLAYER_DISCONNECT_NETWORK)))
            }
            return true
        }
        return false
    }

    override fun readyPlay(accountId: String, roomId: String?) {
        if (!roomId.isNullOrEmpty()) {
            synchronized(this.listRoom) {
                val gameRoom = this.listRoom[UUID.fromString(roomId)]
                if (gameRoom != null) {
                    gameRoom.listPlayerInfo.find { it.accountId == accountId }!!.isReadyPlay = true
                    this.listRoom[gameRoom.gameRoomId] = gameRoom
                    val payload = ResponseDto(gameRoom, StatusDto(MessageKey.SUCCESS))
                    sendTo(gameRoom.listPlayerInfo[0].accountId, payload)
                    sendTo(gameRoom.listPlayerInfo[1].accountId, payload)

                    if (gameRoom.listPlayerInfo[0].isReadyPlay && gameRoom.listPlayerInfo[1].isReadyPlay) {
                        val gameRoomId = gameRoom.gameRoomId
                        val playerOne = gameRoom.listPlayerInfo[0]
                        val playerTwo = gameRoom.listPlayerInfo[1]

                        matchService.createMatch(gameRoomId, UUID.fromString(playerOne.accountId),
                                UUID.fromString(playerTwo.accountId),
                                playerOne.userProfileDto.point!!,
                                playerTwo.userProfileDto.point!!)
                    }
                }
            }
        }
    }

    override fun getListRoom(): HashMap<UUID, GameRoom> {
        return this.listRoom
    }

}