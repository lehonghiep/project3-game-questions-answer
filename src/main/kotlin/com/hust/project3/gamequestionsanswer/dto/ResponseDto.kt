package com.hust.project3.gamequestionsanswer.dto

data class ResponseDto<T>(var data: T?,
                          var statusDto: StatusDto)