package com.hust.project3.gamequestionsanswer.service.impl

import com.hust.project3.gamequestionsanswer.util.PasswordEncoder
import com.hust.project3.gamequestionsanswer.util.Validator
import com.hust.project3.gamequestionsanswer.constant.PointConstants
import com.hust.project3.gamequestionsanswer.constant.MessageKey
import com.hust.project3.gamequestionsanswer.database.entity.Account
import com.hust.project3.gamequestionsanswer.database.entity.UserProfile
import com.hust.project3.gamequestionsanswer.database.repository.AccountRepository
import com.hust.project3.gamequestionsanswer.database.repository.UserProfileRepository
import com.hust.project3.gamequestionsanswer.dto.*
import com.hust.project3.gamequestionsanswer.exception.ServiceException
import com.hust.project3.gamequestionsanswer.service.AccountService
import com.hust.project3.gamequestionsanswer.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Service
class AccountServiceImpl : AccountService {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var profileRepository: UserProfileRepository

    override fun findById(id: UUID): Account? {
        return accountRepository.findById(id).get()
    }

    override fun findByUsername(username: String): Account? {
        return accountRepository.findByUsername(username)
    }

    override fun login(accountDto: AccountDto): ResponseDto<LoginResultDto> {
        val username = accountDto.username
        val password = accountDto.password

        validateUserPassword(username, password)

        val isExisted = accountIsExisted(username)
        if (!isExisted) {
            throw ServiceException(MessageKey.LOGIN_ACCOUNT_NOT_EXISTED)
        }

        val account = accountRepository.findByUsername(username)

        if (!PasswordEncoder.validatePassword(password, account.password)) {
            throw ServiceException(MessageKey.PASSWORD_INVALID)
        }

        val profileDto = createProfileDto(account)

        val token = JwtUtil.createJWT(account.id)
        return ResponseDto(LoginResultDto(username, account.id, profileDto, token),
                StatusDto(MessageKey.SUCCESS))
    }

    private fun createProfileDto(account: Account): UserProfileDto {
        val profile = profileRepository.findById(account.userProfile.id).get()
        val profileDto = UserProfileDto(
                profile.fullName,
                profile.dateOfBirth,
                profile.point,
                profile.avatar
        )

        return profileDto
    }

    override fun accountIsExisted(username: String): Boolean {
        return accountRepository.existsAccountByUsername(username)
    }

    override fun register(registerInfoDto: RegisterInfoDto): ResponseDto<*> {
        val username = registerInfoDto.username
        val password = registerInfoDto.password

        validateUserPassword(username, password)

        //validate username is Existed
        val isExisted = accountIsExisted(username)
        if (isExisted) {
            throw ServiceException(MessageKey.REGISTER_ACCOUNT_EXISTED)
        }

        val account = createAccount(username, password)
        val profile = createProfile(registerInfoDto)

        account.userProfile = profile

        accountRepository.save(account)
        return ResponseDto(null, StatusDto(MessageKey.SUCCESS))
    }

    private fun createAccount(username: String, password: String): Account {
        val account = Account()
        account.username = username
        account.password = PasswordEncoder.encryptPassword(password)
        account.online = false
        return account
    }

    private fun createProfile(registerInfoDto: RegisterInfoDto): UserProfile {
        val userProfile = UserProfile()
        userProfile.fullName = registerInfoDto.fullName
        userProfile.point = PointConstants.DEFAULT_POINT
        return userProfile
    }

    private fun validateUserPassword(username: String, password: String) {
        if (!Validator.validateUsername(username)) {
            throw ServiceException(MessageKey.LOGIN_USERNAME_WRONG)
        }
        if (!Validator.validatorPassword(password)) {
            throw ServiceException(MessageKey.LOGIN_PASSWORD_WRONG)
        }
    }

    override fun online(accountOnlineDto: AccountOnlineDto): ResponseDto<*> {
        val account: Account = accountRepository.findById(accountOnlineDto.accountId).get()
        account.online = accountOnlineDto.isOnline
        accountRepository.save(account)
        return ResponseDto(null, StatusDto(MessageKey.SUCCESS))
    }

}