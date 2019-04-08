package com.hust.project3.gamequestionsanswer.database.entity

import com.hust.project3.gamequestionsanswer.util.AnswerConvertor
import com.hust.project3.gamequestionsanswer.base.BaseEntity
import javax.persistence.*

@Access(AccessType.FIELD)
@Entity
@Table(name = "question")
class Question : BaseEntity() {
    @Column(name = "question")
    lateinit var question: String

    @Column(name = "answer")
    @Convert(converter = AnswerConvertor::class)
    var answers: MutableList<String> = mutableListOf()

    @Column(name = "true_answer")
    lateinit var trueAnswer: String

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_type_id")
    var questionType: QuestionType? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_level_id")
    var questionLevel: QuestionLevel? = null

}