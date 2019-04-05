package com.hust.project3.gamequestionsanswer.dto

data class PlayerInfoDto(var accountId: String,
                         var isReadyPlay: Boolean,
                         var userProfileDto: UserProfileDto) {
}