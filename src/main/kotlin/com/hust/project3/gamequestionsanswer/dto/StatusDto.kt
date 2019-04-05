package com.hust.project3.gamequestionsanswer.dto

import com.hust.project3.gamequestionsanswer.constant.MessageKey

data class StatusDto(var success: Boolean,
                     var messageKey: String,
                     var additionalInfo: Any? = null) {
    constructor(messageKey: MessageKey) : this(true, messageKey.key, null)
    constructor(messageKey: MessageKey, additionalInfo: Any?) : this(false, messageKey.key, additionalInfo)

}