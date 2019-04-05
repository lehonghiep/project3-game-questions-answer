package com.elomath.pro.controller

import com.elomath.pro.constant.MessageKey
import com.elomath.pro.dto.ResponseDto
import com.elomath.pro.dto.StatusDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app-version")
class AppVersioningController {

    @PostMapping("/check")
    fun checkAppVersioning(): ResponseDto<Any> {
        return ResponseDto(null, StatusDto(MessageKey.VERSION_LATEST))
    }

}