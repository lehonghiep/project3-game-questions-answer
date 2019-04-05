package com.elomath.pro.service.impl

import com.elomath.pro.constant.EloConstants
import com.elomath.pro.constant.MessageKey
import com.elomath.pro.database.entity.Account
import com.elomath.pro.database.entity.UserProfile
import com.elomath.pro.database.repository.AccountRepository
import com.elomath.pro.database.repository.UserProfileRepository
import com.elomath.pro.dto.*
import com.elomath.pro.exception.ServiceException
import com.elomath.pro.service.AccountService
import com.elomath.pro.util.JwtUtil
import com.elomath.pro.util.PasswordEncoder
import com.elomath.pro.util.Validator
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

    override fun findByPhoneNumber(phoneNumber: String): Account? {
        return accountRepository.findByPhoneNumber(phoneNumber)
    }

    override fun login(accountDto: AccountDto): ResponseDto<LoginResultDto> {
        val username = accountDto.phoneNumber
        val password = accountDto.password

        validateUserPassword(username, password)

        val isExisted = accountIsExisted(username)
        if (!isExisted) {
            throw ServiceException(MessageKey.LOGIN_ACCOUNT_NOT_EXISTED)
        }

        val account = accountRepository.findByPhoneNumber(username)

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
                profile.elo,
                profile.avatar
        )

        return profileDto
    }

    override fun accountIsExisted(username: String): Boolean {
        return accountRepository.existsAccountByPhoneNumber(username)
    }

    override fun register(registerInfoDto: RegisterInfoDto): ResponseDto<*> {
        val phoneNumber = registerInfoDto.phoneNumber
        val password = registerInfoDto.password

        validateUserPassword(phoneNumber, password)

        //validate phoneNumber is Existed
        val isExisted = accountIsExisted(phoneNumber)
        if (isExisted) {
            throw ServiceException(MessageKey.REGISTER_ACCOUNT_EXISTED)
        }

        val account = createAccount(phoneNumber, password)
        val profile = createProfile(registerInfoDto)

        account.userProfile = profile

        accountRepository.save(account)
        return ResponseDto(null, StatusDto(MessageKey.SUCCESS))
    }

    private fun createAccount(username: String, password: String): Account {
        val account = Account()
        account.phoneNumber = username
        account.password = PasswordEncoder.encryptPassword(password)
        account.online = false
        return account
    }

    private fun createProfile(registerInfoDto: RegisterInfoDto): UserProfile {
        val userProfile = UserProfile()
        userProfile.fullName = registerInfoDto.fullName
        userProfile.elo = EloConstants.DEFAULT_ELO
        return userProfile
    }

    private fun validateUserPassword(phoneNumber: String, password: String) {
        if (!Validator.validateUsername(phoneNumber)) {
            throw ServiceException(MessageKey.LOGIN_PHONENUMBER_WRONG)
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