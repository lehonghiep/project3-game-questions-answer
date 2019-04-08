package com.hust.project3.gamequestionsanswer.service

import com.hust.project3.gamequestionsanswer.database.entity.Match
import com.hust.project3.gamequestionsanswer.dto.AnswerDto
import java.util.*

interface MatchService {
    fun createMatch(roomId: UUID, playerOneId: UUID,
                    playerTwoId: UUID,
                    pointPlayerOne: Int,
                    pointPlayerTwo: Int)

    fun removeMatch(roomId: UUID)

    fun receiveAnswer(roomId: UUID, accountId: UUID, answerDto: AnswerDto)

    fun playerGiveUp(roomId: UUID?, accountId: UUID)

    fun saveMatch(match: Match)

    fun updatePoint(playerOneId: UUID, playerTwoId: UUID, pointPlayerOne: Int, pointPlayerTwo: Int)

}