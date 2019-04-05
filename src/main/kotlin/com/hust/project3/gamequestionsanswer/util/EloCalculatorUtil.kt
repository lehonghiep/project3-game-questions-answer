package com.elomath.pro.util

import com.elomath.pro.constant.EloConstants
import com.elomath.pro.constant.MatchConstant

object EloCalculatorUtil {
    fun scoreElo(preEloPlayerOne: Int, preEloPlayerTwo: Int, resultOfPlayerOne: String): MutableList<Int> {
        val qa = Math.pow(10.0, (preEloPlayerOne.toDouble() / 400.0))
        val qb = Math.pow(10.0, (preEloPlayerTwo.toDouble() / 400.0))
        val sumQaQb = qa + qb
        val eloEquationPlayerOne = qa / sumQaQb
        val eloEquationPlayerTwo = qb / sumQaQb
        val constantKPlayerOne = getConstantKByElo(preEloPlayerOne).toDouble()
        val constantKPlayerTwo = getConstantKByElo(preEloPlayerTwo).toDouble()
        val listResultElo = mutableListOf<Int>()
        when (resultOfPlayerOne) {
            MatchConstant.PLAYER_WIN -> {
                val latestEloPlayerOne = (preEloPlayerOne.toDouble() + constantKPlayerOne * (1.0 - eloEquationPlayerOne)).toInt()
                val latestEloPlayerTwo = (preEloPlayerTwo.toDouble() + constantKPlayerTwo * (0.0 - eloEquationPlayerTwo)).toInt()
                listResultElo.add(latestEloPlayerOne)
                listResultElo.add(latestEloPlayerTwo)
            }
            MatchConstant.PLAYER_DRAW -> {
                val latestEloPlayerOne = (preEloPlayerOne.toDouble() + constantKPlayerOne * (0.5 - eloEquationPlayerOne)).toInt()
                val latestEloPlayerTwo = (preEloPlayerTwo.toDouble() + constantKPlayerTwo * (0.5 - eloEquationPlayerTwo)).toInt()
                listResultElo.add(latestEloPlayerOne)
                listResultElo.add(latestEloPlayerTwo)
            }
            MatchConstant.PLAYER_LOSE -> {
                val latestEloPlayerOne = (preEloPlayerOne.toDouble() + constantKPlayerOne * (0.0 - eloEquationPlayerOne)).toInt()
                val latestEloPlayerTwo = (preEloPlayerTwo.toDouble() + constantKPlayerTwo * (1.0 - eloEquationPlayerTwo)).toInt()
                listResultElo.add(latestEloPlayerOne)
                listResultElo.add(latestEloPlayerTwo)
            }
        }
        return listResultElo
    }

    private fun getConstantKByElo(eloOfPlayer: Int): Int {
        var constantsK = 0
        when {
            eloOfPlayer < 1600 -> {
                constantsK = EloConstants.CONSTANTS_K_OF_PLAYER_BELOW_1600_ELO
            }
            eloOfPlayer < 2000 -> {
                constantsK = EloConstants.CONSTANTS_K_OF_PLAYER_BELOW_2000_ELO
            }
            eloOfPlayer < 2400 -> {
                constantsK = EloConstants.CONSTANTS_K_OF_PLAYER_BELOW_2400_ELO
            }
            eloOfPlayer >= 2400 -> {
                constantsK = EloConstants.CONSTANTS_K_OF_PLAYER_ABOVE_2400_ELO
            }
        }
        return constantsK
    }

}