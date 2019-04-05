package com.elomath.pro.service.impl

import com.elomath.pro.constant.MessageKey
import com.elomath.pro.database.entity.UserProfile
import com.elomath.pro.database.repository.AccountRepository
import com.elomath.pro.database.repository.UserProfileRepository
import com.elomath.pro.dto.ResponseDto
import com.elomath.pro.dto.StatusDto
import com.elomath.pro.dto.UpdateUserProfileInfoDto
import com.elomath.pro.dto.UserProfileDto
import com.elomath.pro.service.UserProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Service
class UserProfileServiceImpl : UserProfileService {
    override fun getUserProfileByAccountId(accountId: String): UserProfileDto {
        val profile = userProfileByAccountId(accountId)
        return UserProfileDto(profile.fullName,
                profile.dateOfBirth,
                profile.elo,
                profile.avatar)

    }

    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    override fun updateUserProfile(updateUserProfileInfoDto: UpdateUserProfileInfoDto,
                                   accountId: String): ResponseDto<*> {

        val profile = userProfileByAccountId(accountId)

        updateUserProfile(updateUserProfileInfoDto, profile)

        userProfileRepository.save(profile)
        return ResponseDto(null, StatusDto(MessageKey.SUCCESS))
    }

    private fun userProfileByAccountId(accountId: String): UserProfile {
        return accountRepository
                .findById(UUID.fromString(accountId))
                .get().userProfile
    }

    private fun updateUserProfile(updateUserProfileInfoDto: UpdateUserProfileInfoDto, profile: UserProfile) {
        val newFirstName = updateUserProfileInfoDto.fullName
        val newDateOfBirth = updateUserProfileInfoDto.dateOfBirth
        val newAvatar = updateUserProfileInfoDto.avatar

        if (!newFirstName.isNullOrEmpty())
            profile.fullName = newFirstName!!
        if (newDateOfBirth != null)
            profile.dateOfBirth = newDateOfBirth
        if (!newAvatar.isNullOrEmpty())
            profile.avatar = newAvatar
    }


}