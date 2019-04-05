package com.elomath.pro.util

class Validator {
    companion object {
        fun validateUsername(username: String): Boolean {
            if (username.isEmpty() || username.isNullOrBlank()) {
                return false
            }
            return true
        }

        fun validatorPassword(password: String): Boolean {
            if (password.isEmpty()
                    || password.isNullOrBlank()
                    || password.length < 6)
                return false
            return true
        }
    }

}