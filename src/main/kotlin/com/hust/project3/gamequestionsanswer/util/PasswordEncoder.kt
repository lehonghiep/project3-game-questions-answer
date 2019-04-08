package com.hust.project3.gamequestionsanswer.util

import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
class PasswordEncoder {

    companion object {
        var passwordEncoder = BCryptPasswordEncoder()
        fun encryptPassword(plainText: String): String {
            return passwordEncoder.encode(plainText)
        }

        fun validatePassword(plainText: String, cipherText: String): Boolean {
            return passwordEncoder.matches(plainText, cipherText)
        }
    }

}