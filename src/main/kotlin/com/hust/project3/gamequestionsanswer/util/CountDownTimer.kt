package com.hust.project3.gamequestionsanswer.util

import java.util.*

class CountDownTimer(var interval: Long, var period: Long, var iCountDownAction: ICountDownAction) {
    var timer: Timer = Timer()
    var timerTask = object : TimerTask() {
        override fun run() {

            if (interval < 0) {
                timer.cancel()
                iCountDownAction.afterFinish()
                return
            }
            iCountDownAction.afterPeriodTime(interval, object : IActionStopCountDown {
                override fun cancel() {
                    timer.cancel()
                    iCountDownAction.afterFinish()
                }
            })
            interval -= period
        }
    }

    fun run() {
        timer.scheduleAtFixedRate(timerTask, 0, period * 1000)
    }

    fun cancel() {
        timer.cancel()
    }

}

interface ICountDownAction {
    fun afterPeriodTime(currentTime: Long, iActionStopCountDown: IActionStopCountDown)

    fun afterFinish()
}

interface IActionStopCountDown {
    fun cancel()
}