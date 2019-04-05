package com.hust.project3.gamequestionsanswer.database.entity

import com.hust.project3.gamequestionsanswer.base.BaseEntity
import java.util.*
import javax.persistence.*

@Access(AccessType.FIELD)
@Entity
@Table(name = "user_profile")
class UserProfile : BaseEntity() {
    @Column(name = "full_name")
    lateinit var fullName: String

    @Column(name = "date_of_birth")
    var dateOfBirth: Date? = null

    @Column(name = "elo")
    var elo: Int? = null

    @Column(name = "avatar")
    var avatar: String? = null

}