package com.hust.project3.gamequestionsanswer.dto

import java.util.*

data class LoginResultDto(var phoneNumber: String,
                          var accountId: UUID,
                          var userProfileDto: UserProfileDto,
                          var token: String)