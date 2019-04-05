package com.hust.project3.gamequestionsanswer.database.entity

import com.hust.project3.gamequestionsanswer.base.BaseEntity
import javax.persistence.*

@Access(AccessType.FIELD)
@Entity
@Table(name = "question_type")
class QuestionType : BaseEntity() {
    @Column(name = "name")
    lateinit var name: String

    @Column(name = "description")
    var description: String? = null

//    @OneToMany(mappedBy = "questionType", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
//    var questions: List<Question> = emptyList()

}