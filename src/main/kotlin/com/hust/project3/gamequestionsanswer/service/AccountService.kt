package com.hust.project3.gamequestionsanswer.service

import com.hust.project3.gamequestionsanswer.database.entity.Account
import com.hust.project3.gamequestionsanswer.dto.*
import java.util.*

interface AccountService {
    fun accountIsExisted(username: String): Boolean

    fun register(registerInfoDto: RegisterInfoDto): ResponseDto<*>

    fun login(accountDto: AccountDto): ResponseDto<LoginResultDto>

    fun findById(id: UUID): Account?

    fun findByUsername(username: String): Account?

    fun online(accountOnlineDto: AccountOnlineDto): ResponseDto<*>

}