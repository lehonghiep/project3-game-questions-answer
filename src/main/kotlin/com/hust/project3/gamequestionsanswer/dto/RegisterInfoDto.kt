package com.hust.project3.gamequestionsanswer.dto

import com.hust.project3.gamequestionsanswer.base.BaseDto

data class RegisterInfoDto(var phoneNumber: String,
                           var password: String,
                           var fullName: String)