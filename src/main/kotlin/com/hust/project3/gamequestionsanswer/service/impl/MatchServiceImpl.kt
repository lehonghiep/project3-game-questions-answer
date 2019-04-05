package com.elomath.pro.service.impl

import com.elomath.pro.database.entity.Match
import com.elomath.pro.database.repository.MatchRepository
import com.elomath.pro.database.repository.UserProfileRepository
import com.elomath.pro.dto.AnswerDto
import com.elomath.pro.log.LogFactory
import com.elomath.pro.service.GamePlayService
import com.elomath.pro.service.MatchService
import com.elomath.pro.service.QuestionService
import com.elomath.pro.util.MatchManager
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
        override fun saveMatchResult(match: Match, eloPlayerOne: Int, eloPlayerTwo: Int) {
            saveMatch(match)
            updateElo(match.playerOne, match.playerTwo, eloPlayerOne, eloPlayerTwo)
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
                             eloPlayerOne: Int, eloPlayerTwo: Int) {
        val matchManager = MatchManager(simpMessageSendingOperations,
                questionService, simpleBrokerPrefix,
                iActionMatchManager,
                roomId, playerOne, playerTwo, eloPlayerOne, eloPlayerTwo)
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

    override fun updateElo(playerOneId: UUID, playerTwoId: UUID, eloPlayerOne: Int, eloPlayerTwo: Int) {
        userProfileRepository.updateEloByAccountId(eloPlayerOne, playerOneId)
        userProfileRepository.updateEloByAccountId(eloPlayerTwo, playerTwoId)
    }

    override fun playerGiveUp(roomId: UUID?, accountId: UUID) {
        val matchManager = listMatchManager[roomId]
        matchManager?.playerGiveUp(accountId)
    }
}

interface IActionMatchManager {
    fun saveMatchResult(match: Match, eloPlayerOne: Int, eloPlayerTwo: Int)

    fun removeMatchManager(roomId: UUID)
}