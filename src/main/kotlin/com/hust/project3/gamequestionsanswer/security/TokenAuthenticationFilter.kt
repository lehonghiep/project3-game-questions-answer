package com.hust.project3.gamequestionsanswer.security

import com.google.gson.Gson
import com.hust.project3.gamequestionsanswer.constant.Key
import com.hust.project3.gamequestionsanswer.constant.SecurityConstant
import com.hust.project3.gamequestionsanswer.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var accountService: AccountService

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        try {
            val authentication: Authentication
            val authToken = request.getHeader(SecurityConstant.HEADER_AUTHORIZATION)
            if (!authToken.isNullOrEmpty()) {
                val accountId = JwtUtil.getIdFromToken(authToken)
                val account = accountService.findById(UUID.fromString(accountId))
                if (account == null) {
                    throw ServiceException(MessageKey.REQUEST_TOKEN_INVALID_OR_NOT_AVAILABLE)
                } else {
                    request.setAttribute(Key.ACCOUNT_ID, accountId)
                    authentication = UsernamePasswordAuthenticationToken(account, null, Collections.emptyList())
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (e: ServiceException) {
            val messageKey = e.messageKey
            val responseDto = ResponseDto(null, StatusDto(false, messageKey.key))
            val responseJson = Gson().toJson(responseDto)
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.outputStream.write(responseJson.toByteArray())
            response.outputStream.flush()
            response.outputStream.close()
            return
        }
        filterChain.doFilter(request, response)
        SecurityContextHolder.getContext().authentication = null
    }

}