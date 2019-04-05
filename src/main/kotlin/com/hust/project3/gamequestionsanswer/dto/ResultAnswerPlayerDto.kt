package com.hust.project3.gamequestionsanswer.dto

import java.util.*

class ResultAnswerPlayerDto(var accountId: UUID,
                            var orderNumberQuestion: Int,
                            var isCorrect: Boolean,
                            var currentScore: Int)