package com.elomath.pro.service

import com.elomath.pro.database.entity.Match
import com.elomath.pro.dto.AnswerDto
import com.elomath.pro.dto.PlayerInfoDto
import java.util.*

interface MatchService {
    fun createMatch(roomId: UUID, playerOneId: UUID,
                    playerTwoId: UUID,
                    eloPlayerOne: Int,
                    eloPlayerTwo: Int)

    fun removeMatch(roomId: UUID)

    fun receiveAnswer(roomId: UUID, accountId: UUID, answerDto: AnswerDto)

    fun playerGiveUp(roomId: UUID?, accountId: UUID)

    fun saveMatch(match: Match)

    fun updateElo(playerOneId: UUID, playerTwoId: UUID, eloPlayerOne: Int, eloPlayerTwo: Int)
}