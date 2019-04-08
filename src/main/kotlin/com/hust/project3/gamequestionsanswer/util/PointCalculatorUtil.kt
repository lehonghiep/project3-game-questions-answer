package com.hust.project3.gamequestionsanswer.util

import com.hust.project3.gamequestionsanswer.constant.PointConstants
import com.hust.project3.gamequestionsanswer.constant.MatchConstant

object PointCalculatorUtil {
    fun calculate(prePointPlayerOne: Int, prePointPlayerTwo: Int, resultOfPlayerOne: String): MutableList<Int> {
        val qa = Math.pow(10.0, (prePointPlayerOne.toDouble() / 400.0))
        val qb = Math.pow(10.0, (prePointPlayerTwo.toDouble() / 400.0))
        val sumQaQb = qa + qb
        val pointEquationPlayerOne = qa / sumQaQb
        val pointEquationPlayerTwo = qb / sumQaQb
        val constantKPlayerOne = getConstantKByPoint(prePointPlayerOne).toDouble()
        val constantKPlayerTwo = getConstantKByPoint(prePointPlayerTwo).toDouble()
        val listResultPoint = mutableListOf<Int>()
        when (resultOfPlayerOne) {
            MatchConstant.PLAYER_WIN -> {
                val latestPointPlayerOne = (prePointPlayerOne.toDouble() + constantKPlayerOne * (1.0 - pointEquationPlayerOne)).toInt()
                val latestPointPlayerTwo = (prePointPlayerTwo.toDouble() + constantKPlayerTwo * (0.0 - pointEquationPlayerTwo)).toInt()
                listResultPoint.add(latestPointPlayerOne)
                listResultPoint.add(latestPointPlayerTwo)
            }
            MatchConstant.PLAYER_DRAW -> {
                val latestPointPlayerOne = (prePointPlayerOne.toDouble() + constantKPlayerOne * (0.5 - pointEquationPlayerOne)).toInt()
                val latestPointPlayerTwo = (prePointPlayerTwo.toDouble() + constantKPlayerTwo * (0.5 - pointEquationPlayerTwo)).toInt()
                listResultPoint.add(latestPointPlayerOne)
                listResultPoint.add(latestPointPlayerTwo)
            }
            MatchConstant.PLAYER_LOSE -> {
                val latestPointPlayerOne = (prePointPlayerOne.toDouble() + constantKPlayerOne * (0.0 - pointEquationPlayerOne)).toInt()
                val latestPointPlayerTwo = (prePointPlayerTwo.toDouble() + constantKPlayerTwo * (1.0 - pointEquationPlayerTwo)).toInt()
                listResultPoint.add(latestPointPlayerOne)
                listResultPoint.add(latestPointPlayerTwo)
            }
        }
        return listResultPoint
    }

    private fun getConstantKByPoint(pointOfPlayer: Int): Int {
        var constantsK = 0
        when {
            pointOfPlayer < 1600 -> {
                constantsK = PointConstants.CONSTANTS_K_OF_PLAYER_BELOW_1600_POINT
            }
            pointOfPlayer < 2000 -> {
                constantsK = PointConstants.CONSTANTS_K_OF_PLAYER_BELOW_2000_POINT
            }
            pointOfPlayer < 2400 -> {
                constantsK = PointConstants.CONSTANTS_K_OF_PLAYER_BELOW_2400_POINT
            }
            pointOfPlayer >= 2400 -> {
                constantsK = PointConstants.CONSTANTS_K_OF_PLAYER_ABOVE_2400_POINT
            }
        }
        return constantsK
    }

}