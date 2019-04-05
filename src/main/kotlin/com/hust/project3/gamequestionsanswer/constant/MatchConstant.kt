package com.elomath.pro.constant

object MatchConstant {
    const val NUMBER_OF_OPENING_QUESTIONS = 10
    const val NUMBER_OF_MIDDLE_QUESTIONS = 5
    const val NUMBER_OF_ENDGAME_QUESTIONS = 3

    const val OPENING_FIRST_PLUS_POINT = 2
    const val OPENING_SECOND_PLUS_POINT = 1
    const val OPENING_MINUS_POINT = 1

    const val MIDDLE_FIRST_PLUS_POINT = 4
    const val MIDDLE_SECOND_PLUS_POINT = 2
    const val MIDDLE_MINUS_POINT = 2

    const val ENDGAME_FIRST_PLUS_POINT = 6
    const val ENDGAME_SECOND_PLUS_POINT = 3
    const val ENDGAME_MINUS_POINT = 3

    const val COUNT_DOWN_TIME_OPENING = 60L
    const val COUNT_DOWN_TIME_MIDDLE = 120L
    const val COUNT_DOWN_TIME_ENDGAME = 180L

    const val PLAYER_WIN = "WIN"
    const val PLAYER_DRAW = "DRAW" //ket qua hoa
    const val PLAYER_LOSE = "LOSE"

    const val END_MATCH = "END_MATCH"
    const val PLAYER_REQUIRE_LOSE = "PLAYER_REQUIRE_LOSE"
    const val NOT_ALLOWABLE_CONTINUE_PLAY = "NOT_ALLOWABLE_CONTINUE_PLAY"

    const val TIME_DELAY_BETWEEN_QUESTIONS= 1000L
    const val TIME_DELAY_BETWEEN_QUESTION_LEVEL= 3000L
}