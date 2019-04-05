package com.elomath.pro.service

import com.elomath.pro.dto.QuestionDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


interface QuestionService {
    fun getRandomQuestionsByQuestionLevel(questionLevel: String, numOfQuestions: Int): MutableList<QuestionDto>

    fun getRandomQuestionsAllLevel(numOfOpening: Int, numOfMiddle: Int, numOfEndGame: Int): MutableList<QuestionDto>
}