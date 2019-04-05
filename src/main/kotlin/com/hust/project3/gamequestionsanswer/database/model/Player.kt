package com.hust.project3.gamequestionsanswer.database.model

import java.util.*

class Player(var accountId: UUID,
             var score: Int) {
    var numOfConsecutiveQuestionNotAnswer: Int = 0

    fun increaseNumOfQuestionNotAnswer() {
        numOfConsecutiveQuestionNotAnswer++
    }

    fun resetNumOfQuestionNotAnswer() {
        numOfConsecutiveQuestionNotAnswer = 0
    }

    fun checkAcceptContinuePlay(): Boolean {
        return numOfConsecutiveQuestionNotAnswer <= 2
    }
}