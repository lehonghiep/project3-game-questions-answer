package com.hust.project3.gamequestionsanswer.security

import com.google.gson.Gson
import com.hust.project3.gamequestionsanswer.constant.MessageKey
import com.hust.project3.gamequestionsanswer.dto.ResponseDto
import com.hust.project3.gamequestionsanswer.dto.StatusDto
import org.hibernate.service.spi.ServiceException
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(ServiceException::class)
    override fun commence(request: HttpServletRequest,
                          response: HttpServletResponse,
                          authException: AuthenticationException) {
        val message = MessageKey.REQUEST_TOKEN_REQUIRED.key
        val responseDto = ResponseDto(null, StatusDto(false, message))
        val responseJson = Gson().toJson(responseDto)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.outputStream.write(responseJson.toByteArray())
        response.outputStream.flush()
        response.outputStream.close()
        return
    }

}