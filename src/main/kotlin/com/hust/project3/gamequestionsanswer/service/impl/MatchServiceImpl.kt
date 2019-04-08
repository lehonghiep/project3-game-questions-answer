package com.hust.project3.gamequestionsanswer.service.impl

import com.hust.project3.gamequestionsanswer.util.MatchManager
import com.hust.project3.gamequestionsanswer.database.entity.Match
import com.hust.project3.gamequestionsanswer.database.repository.MatchRepository
import com.hust.project3.gamequestionsanswer.database.repository.UserProfileRepository
import com.hust.project3.gamequestionsanswer.dto.AnswerDto
import com.hust.project3.gamequestionsanswer.log.LogFactory
import com.hust.project3.gamequestionsanswer.service.GamePlayService
import com.hust.project3.gamequestionsanswer.service.MatchService
import com.hust.project3.gamequestionsanswer.service.QuestionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Service
class MatchServiceImpl : MatchService {
    @Autowired
    private lateinit var matchRepository: MatchRepository

    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository

    @Autowired
    private lateinit var simpMessageSendingOperations: SimpMessageSendingOperations

    @Autowired
    private lateinit var gamePlayService: GamePlayService

    @Value("\${websocket.destination.prefix.broker}")
    private lateinit var simpleBrokerPrefix: String

    @Autowired
    lateinit var questionService: QuestionService

    private var listMatchManager: MutableMap<UUID, MatchManager> = mutableMapOf()

    private var iActionMatchManager: IActionMatchManager = object : IActionMatchManager {
        override fun saveMatchResult(match: Match, pointPlayerOne: Int, pointPlayerTwo: Int) {
            saveMatch(match)
            updatePoint(match.playerOne, match.playerTwo, pointPlayerOne, pointPlayerTwo)
        }

        override fun removeMatchManager(roomId: UUID) {
            removeMatch(roomId)
            removeRoom(roomId)
        }

    }

    private fun removeRoom(roomId: UUID) {
        var currentListRoom = gamePlayService.getListRoom()
        var sizeListRoom = currentListRoom.size
        LogFactory.logger.info("Num Of Room before remove room:" + sizeListRoom)
        if (currentListRoom.containsKey(roomId)) {
            currentListRoom.remove(roomId)
        }
        sizeListRoom = currentListRoom.size
        LogFactory.logger.info("Num Of Room after remove room:" + sizeListRoom)
    }

    override fun createMatch(roomId: UUID, playerOne: UUID, playerTwo: UUID,
                             pointPlayerOne: Int, pointPlayerTwo: Int) {
        val matchManager = MatchManager(simpMessageSendingOperations,
                questionService, simpleBrokerPrefix,
                iActionMatchManager,
                roomId, playerOne, playerTwo, pointPlayerOne, pointPlayerTwo)
        if (listMatchManager.containsKey(roomId)) {
            return
        }
        listMatchManager[roomId] = matchManager
        LogFactory.logger.info("num Of Match:" + listMatchManager.size)
        matchManager.begin()
    }

    override fun removeMatch(roomId: UUID) {
        listMatchManager.remove(roomId)
    }

    override fun saveMatch(match: Match) {
        matchRepository.save(match)
    }

    override fun receiveAnswer(roomId: UUID, accountId: UUID, answerDto: AnswerDto) {
        val matchManager = listMatchManager[roomId]
        matchManager?.receiveAnswer(accountId, answerDto)
    }

    override fun updatePoint(playerOneId: UUID, playerTwoId: UUID, pointPlayerOne: Int, pointPlayerTwo: Int) {
        userProfileRepository.updatePointByAccountId(pointPlayerOne, playerOneId)
        userProfileRepository.updatePointByAccountId(pointPlayerTwo, playerTwoId)
    }

    override fun playerGiveUp(roomId: UUID?, accountId: UUID) {
        val matchManager = listMatchManager[roomId]
        matchManager?.playerGiveUp(accountId)
    }
}

interface IActionMatchManager {
    fun saveMatchResult(match: Match, pointPlayerOne: Int, pointPlayerTwo: Int)

    fun removeMatchManager(roomId: UUID)
}