package com.hust.project3.gamequestionsanswer.util

class Validator {
    companion object {
        fun validateUsername(username: String?): Boolean {
            if (username.isNullOrEmpty()) {
                return false
            }
            if (username.isBlank() || username.length < 6) {
                return false
            }
            return true
        }

        fun validatorPassword(password: String?): Boolean {
            if (password.isNullOrEmpty()) {
                return false
            }
            if (password.isBlank() || password.length < 6) {
                return false
            }
            return true
        }
    }

}