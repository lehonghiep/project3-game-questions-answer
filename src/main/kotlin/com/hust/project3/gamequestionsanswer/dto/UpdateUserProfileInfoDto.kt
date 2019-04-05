package com.hust.project3.gamequestionsanswer.dto

import com.hust.project3.gamequestionsanswer.base.BaseDto
import java.util.*

data class UpdateUserProfileInfoDto(var fullName: String?,
                                    var dateOfBirth: Date?,
                                    var avatar: String?) : BaseDto()