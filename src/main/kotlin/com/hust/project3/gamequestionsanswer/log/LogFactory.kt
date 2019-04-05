package com.hust.project3.gamequestionsanswer.log

import org.apache.log4j.Logger

object LogFactory {
    val logger = Logger.getLogger(Thread.currentThread().stackTrace[2].className)

}