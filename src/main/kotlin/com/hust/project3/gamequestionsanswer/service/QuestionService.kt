package com.hust.project3.gamequestionsanswer.service

import com.hust.project3.gamequestionsanswer.dto.QuestionDto


interface QuestionService {
    fun getRandomQuestionsByQuestionLevel(questionLevel: String, numOfQuestions: Int): MutableList<QuestionDto>

    fun getRandomQuestionsAllLevel(numOfOpening: Int, numOfMiddle: Int, numOfEndGame: Int): MutableList<QuestionDto>

}