package com.hust.project3.gamequestionsanswer.exception

import com.hust.project3.gamequestionsanswer.dto.ResponseDto
import com.hust.project3.gamequestionsanswer.dto.StatusDto
import com.hust.project3.gamequestionsanswer.log.LogFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
@RestController
class ExceptionHandler {
    @ExceptionHandler(value = [(ServiceException::class)])
    fun handleServiceException(e: ServiceException, web: WebRequest): ResponseDto<String> {
        LogFactory.logger.error(e.messageKey.key)
        return ResponseDto(null, StatusDto(false, e.messageKey.key))
    }

}