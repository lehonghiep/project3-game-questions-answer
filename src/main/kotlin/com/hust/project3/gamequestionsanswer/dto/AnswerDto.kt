package com.hust.project3.gamequestionsanswer.dto

data class AnswerDto(var orderNumber: Int,
                     var questionType: String,
                     var answer: String,
                     var responseTime: Int)