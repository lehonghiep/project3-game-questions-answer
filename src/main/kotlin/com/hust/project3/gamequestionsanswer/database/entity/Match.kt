package com.hust.project3.gamequestionsanswer.database.entity

import com.hust.project3.gamequestionsanswer.base.BaseEntity
import java.util.*
import javax.persistence.*

@Access(AccessType.FIELD)
@Entity
@Table(name = "match")
class Match : BaseEntity() {
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "player_one")
//    lateinit var playerOne: Account

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "player_two")
//    lateinit var playerTwo: Account

    @Column(name = "player_one")
    lateinit var playerOne: UUID

    @Column(name = "player_two")
    lateinit var playerTwo: UUID

    @Column(name = "player_one_score")
    var playerOneScore: Int? = null

    @Column(name = "player_two_score")
    var playerTwoScore: Int? = null

    @Column(name = "winner")
    var winner: UUID? = null

}