package com.hust.project3.gamequestionsanswer.base

import java.util.*

abstract class BaseDto() {
    lateinit var id: UUID
    lateinit var createdDate: Date
    lateinit var updatedDate: Date

}