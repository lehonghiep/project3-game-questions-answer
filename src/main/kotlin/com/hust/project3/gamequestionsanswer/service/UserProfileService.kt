package com.elomath.pro.service

import com.elomath.pro.dto.ResponseDto
import com.elomath.pro.dto.UpdateUserProfileInfoDto
import com.elomath.pro.dto.UserProfileDto

interface UserProfileService {
    fun updateUserProfile(updateUserProfileInfoDto: UpdateUserProfileInfoDto,
                          accountId: String): ResponseDto<*>

    fun getUserProfileByAccountId(accountId: String): UserProfileDto
}