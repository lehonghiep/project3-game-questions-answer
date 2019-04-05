package com.hust.project3.gamequestionsanswer.controller

import com.hust.project3.gamequestionsanswer.constant.Key
import com.hust.project3.gamequestionsanswer.dto.ResponseDto
import com.hust.project3.gamequestionsanswer.dto.UpdateUserProfileInfoDto
import com.hust.project3.gamequestionsanswer.log.LogFactory
import com.hust.project3.gamequestionsanswer.service.UserProfileService
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