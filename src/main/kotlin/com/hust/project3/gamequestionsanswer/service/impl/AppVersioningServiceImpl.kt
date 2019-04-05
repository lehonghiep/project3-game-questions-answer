package com.elomath.pro.service.impl

import com.elomath.pro.constant.MessageKey
import com.elomath.pro.database.constant.AppType
import com.elomath.pro.database.repository.AppVersioningRepository
import com.elomath.pro.dto.AppVersioningDto
import com.elomath.pro.dto.ResponseDto
import com.elomath.pro.dto.StatusDto
import com.elomath.pro.service.AppVersioningService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AppVersioningServiceImpl : AppVersioningService {
    @Value("\${app.URL}")
    private lateinit var appURL: String

    @Autowired
    private lateinit var appVersioningRepository: AppVersioningRepository

    override fun checkAppVersioning(appVersioningDto: AppVersioningDto): ResponseDto<Any> {
        val appVersioning = appVersioningRepository.findByAppType(appVersioningDto.appType)
        val compareVersion = appVersioningDto.versioning.compareTo(appVersioning.versioning)
        if (compareVersion < 0) {
            return ResponseDto(null, StatusDto(MessageKey.VERSION_OLD, appURL))
        }
        if (compareVersion > 0) {
            return ResponseDto(null, StatusDto(MessageKey.VERSION_VERSIONING_INVALID, appURL))
        }
        //compareVersion == 0
        return ResponseDto(null, StatusDto(MessageKey.VERSION_LATEST))
    }

    override fun validateAppType(appType: String): Boolean {
        if (appType == AppType.ANDROID.name || appType == AppType.IOS.name) {
            return true
        }
        return false
    }

}