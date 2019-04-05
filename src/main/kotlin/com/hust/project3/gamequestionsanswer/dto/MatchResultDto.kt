package com.hust.project3.gamequestionsanswer.dto

import java.util.*

data class MatchResultDto(var listPlayer: MutableList<ResultPlayerDto>,
                          var winner: UUID?)