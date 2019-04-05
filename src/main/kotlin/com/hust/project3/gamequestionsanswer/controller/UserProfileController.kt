package com.elomath.pro.controller

import com.elomath.pro.constant.Key
import com.elomath.pro.dto.ResponseDto
import com.elomath.pro.dto.UpdateUserProfileInfoDto
import com.elomath.pro.log.LogFactory
import com.elomath.pro.service.UserProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user_profile")
class UserProfileController {
    @Autowired
    private lateinit var userProfileService: UserProfileService

    @PutMapping("/update")
    fun updateProfile(@RequestBody updateUserProfileInfoDto: UpdateUserProfileInfoDto,
                      httpServletRequest: HttpServletRequest): ResponseDto<*> {
        LogFactory.logger.info("update userProfile")
        val id = httpServletRequest.getAttribute(Key.ACCOUNT_ID).toString()
        return userProfileService.updateUserProfile(updateUserProfileInfoDto, id)
    }

}