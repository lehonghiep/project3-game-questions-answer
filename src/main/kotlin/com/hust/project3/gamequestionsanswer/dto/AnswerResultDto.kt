package com.hust.project3.gamequestionsanswer.dto

import java.util.*

data class AnswerResultDto(var orderNumber: Int,
                           var accountId: UUID,
                           var isCorrect: Boolean,
                           var currentScore: Int)