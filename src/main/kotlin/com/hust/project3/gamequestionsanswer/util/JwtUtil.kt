package com.hust.project3.gamequestionsanswer.util

import com.hust.project3.gamequestionsanswer.constant.Key
import com.hust.project3.gamequestionsanswer.constant.MessageKey
import com.hust.project3.gamequestionsanswer.constant.SecurityConstant
import com.hust.project3.gamequestionsanswer.exception.ServiceException
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import java.util.*
import javax.crypto.spec.SecretKeySpec

object JwtUtil {
    fun createJWT(accountId: UUID): String {
        val signatureAlgorithm = SignatureAlgorithm.HS256
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val keySecretBytes = SecurityConstant.SECRET.toByteArray()
        val signingKey = SecretKeySpec(keySecretBytes, signatureAlgorithm.jcaName)
        val builder = Jwts.builder().claim(Key.ACCOUNT_ID, accountId)
                .setIssuedAt(now)
                .setExpiration(generateExpirationDate(nowMillis))
                .signWith(signingKey, signatureAlgorithm)
        return builder.compact()
    }

    fun getClaimsFromToken(token: String?): Claims {
        lateinit var claims: Claims
        try {
            claims = Jwts.parser()
                    .setSigningKey(SecurityConstant.SECRET.toByteArray())
                    .parseClaimsJws(token).body
        } catch (e: SignatureException) {
            throw ServiceException(MessageKey.REQUEST_TOKEN_INVALID_OR_NOT_AVAILABLE)
        } catch (e: ExpiredJwtException) {
            throw ServiceException(MessageKey.REQUEST_TOKEN_EXPIRED)
        } catch (e: MalformedJwtException) {
            throw ServiceException(MessageKey.REQUEST_TOKEN_WRONG)
        } catch (e: JwtException) {
            throw ServiceException(MessageKey.FAILURE)
        }
        return claims
    }

    private fun generateExpirationDate(currentTimeMillis: Long): Date {
        return Date(currentTimeMillis + SecurityConstant.EXPIRATION_TIME)
    }

    fun getIdFromToken(token: String?): String {
        val claims = getClaimsFromToken(token)
        return claims[Key.ACCOUNT_ID].toString()
    }

}