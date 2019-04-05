package com.hust.project3.gamequestionsanswer.dto

data class QuestionDto(var orderNumber: Int,
                       var question: String,
                       var answers: MutableList<String>?,
                       var trueAnswer: String,
                       var questionType: String,
                       var questionLevel: String)