package com.hust.project3.gamequestionsanswer.service

import com.hust.project3.gamequestionsanswer.dto.ResponseDto
import com.hust.project3.gamequestionsanswer.dto.UpdateUserProfileInfoDto
import com.hust.project3.gamequestionsanswer.dto.UserProfileDto

interface UserProfileService {
    fun updateUserProfile(updateUserProfileInfoDto: UpdateUserProfileInfoDto,
                          accountId: String): ResponseDto<*>

    fun getUserProfileByAccountId(accountId: String): UserProfileDto

}