package com.hust.project3.gamequestionsanswer.database.entity

import com.hust.project3.gamequestionsanswer.base.BaseEntity
import javax.persistence.*

@Access(AccessType.FIELD)
@Entity
@Table(name = "account_level")
class AccountLevel : BaseEntity() {
    @Column(name = "level")
    lateinit var level: String

    @Column(name = "max_of_matches")
    var maxOfMatches: Int? = null

    @OneToMany(mappedBy = "accountLevel", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
    var accounts: MutableList<Account> = mutableListOf()

}