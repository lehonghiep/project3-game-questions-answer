package com.hust.project3.gamequestionsanswer.database.model


class QuestionInMatch(var orderNumber: Int?,
                      var question: String,
                      var answers: MutableList<String>?,
                      var trueAnswer: String,
                      var questionType: String,
                      var questionLevel: String,
                      var listPlayersAnswer: MutableList<PlayerAnswer>)