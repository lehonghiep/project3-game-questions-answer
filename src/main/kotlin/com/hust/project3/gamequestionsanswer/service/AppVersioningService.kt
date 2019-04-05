package com.elomath.pro.service

import com.elomath.pro.dto.AppVersioningDto
import com.elomath.pro.dto.ResponseDto

interface AppVersioningService {
    fun checkAppVersioning(appVersioningDto: AppVersioningDto): ResponseDto<Any>

    fun validateAppType(appType: String): Boolean

}