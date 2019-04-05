package com.hust.project3.gamequestionsanswer.exception

import com.hust.project3.gamequestionsanswer.constant.MessageKey

data class ServiceException(val messageKey: MessageKey) : Exception()
