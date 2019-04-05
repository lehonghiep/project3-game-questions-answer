package com.hust.project3.gamequestionsanswer.service

import com.elomath.pro.database.entity.Account
import com.elomath.pro.dto.*
import java.util.*

interface AccountService {
    fun accountIsExisted(username: String): Boolean

    fun register(registerInfoDto: RegisterInfoDto): ResponseDto<*>

    fun login(accountDto: AccountDto): ResponseDto<LoginResultDto>

    fun findById(id: UUID): Account?

    fun findByPhoneNumber(phoneNumber: String): Account?

    fun online(accountOnlineDto: AccountOnlineDto): ResponseDto<*>
    
}