package com.elomath.pro.controller

import com.elomath.pro.constant.Key
import com.elomath.pro.dto.*
import com.elomath.pro.log.LogFactory
import com.elomath.pro.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/account")
class AccountController {
    @Autowired
    private lateinit var accountService: AccountService

    @PostMapping("/register")
    fun register(@RequestBody registerInfoDto: RegisterInfoDto): ResponseDto<*> {
        LogFactory.logger.info("register")
        return accountService.register(registerInfoDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody accountDto: AccountDto): ResponseDto<LoginResultDto> {
        LogFactory.logger.info("login")
        return accountService.login(accountDto)
    }

    @PostMapping("/online")
    fun online(@RequestBody accountOnlineDto: AccountOnlineDto, httpServletRequest: HttpServletRequest): ResponseDto<*> {
        LogFactory.logger.info("online")
        return accountService.online(accountOnlineDto)
    }

}