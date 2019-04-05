package com.hust.project3.gamequestionsanswer.dto

import com.hust.project3.gamequestionsanswer.base.BaseDto
import com.hust.project3.gamequestionsanswer.database.constant.AppType

class AppVersioningDto(var appType: AppType,
                       var versioning: String) : BaseDto() {
}