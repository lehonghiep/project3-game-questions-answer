package com.hust.project3.gamequestionsanswer.database.entity

import com.hust.project3.gamequestionsanswer.base.BaseEntity
import javax.persistence.*

@Access(AccessType.FIELD)
@Entity
@Table(name = "question_level")
class QuestionLevel : BaseEntity() {
    @Column(name = "level")
    var level: String? = null

    @Column(name = "description")
    var description: String? = null

//    @OneToMany(mappedBy = "questionLevel", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
//    var questions: List<Question> = emptyList()

}