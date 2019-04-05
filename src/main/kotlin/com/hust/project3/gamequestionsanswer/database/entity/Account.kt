package com.hust.project3.gamequestionsanswer.database.entity

import com.elomath.pro.database.entity.AccountLevel
import com.elomath.pro.database.entity.UserProfile
import com.hust.project3.gamequestionsanswer.base.BaseEntity
import javax.persistence.*

@Access(AccessType.FIELD)
@Entity
@Table(name = "account")
class Account : BaseEntity() {
    @Column(name = "phone_number")
    lateinit var phoneNumber: String

    @Column(name = "password")
    lateinit var password: String

    @OneToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "user_profile_id")
    lateinit var userProfile: UserProfile

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_level_id")
    lateinit var accountLevel: AccountLevel

    @Column(name = "online")
    var online: Boolean? = null

//    @OneToMany(mappedBy = "listPlayer", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
//    var playerOneMatchs: MutableList<Match> = mutableListOf()
//
//    @OneToMany(mappedBy = "playerTwo", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
//    var playerTwoMatchs: MutableList<Match> = mutableListOf()

}