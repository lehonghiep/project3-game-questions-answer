package com.hust.project3.gamequestionsanswer.database.repository

import com.hust.project3.gamequestionsanswer.database.entity.Question
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Repository
interface QuestionRepository : PagingAndSortingRepository<Question, UUID> {
//    @Query("SELECT * FROM question q, question_level " +
//            "WHERE q.question_level_id=question_level.id AND question_level.level=?1 ORDER BY RANDOM() LIMIT ?2"
//            , nativeQuery = true)
//    fun getRandomQuestionsByQuestionLevel(questionLevel: String, numOfQuestion: Int): MutableList<Question>

    @Query("SELECT * FROM question q, question_level, question_type " +
            "WHERE q.question_level_id=question_level.id AND question_type.id=q.question_type_id " +
            "AND question_level.level=?1 " +
            "AND question_type.name='MULTI_CHOICE' ORDER BY RANDOM() LIMIT ?2"
            , nativeQuery = true)
    fun getRandomQuestionsByQuestionLevel(questionLevel: String, numOfQuestion: Int): MutableList<Question>

//    @Query("(SELECT * FROM question q, question_level\n" +
//            "WHERE q.question_level_id=question_level.id \n" +
//            "AND question_level.level='OPENING' ORDER BY RANDOM() LIMIT ?1)\n" +
//            "UNION ALL\n" +
//            "(SELECT * FROM question q, question_level\n" +
//            "WHERE q.question_level_id=question_level.id \n" +
//            "AND question_level.level='MIDDLE' ORDER BY RANDOM() LIMIT ?2)\n" +
//            "UNION ALL\n" +
//            "(SELECT * FROM question q, question_level\n" +
//            "WHERE q.question_level_id=question_level.id \n" +
//            "AND question_level.level='ENDGAME' ORDER BY RANDOM() LIMIT ?3);", nativeQuery = true)
//    fun getRandomQuestionsAllLevel(numOfOpening: Int, numOfMiddle: Int, numOfEndGame: Int): MutableList<Question>


    @Query("(SELECT * FROM question q, question_level, question_type\n" +
            "WHERE q.question_level_id=question_level.id\n" +
            "AND question_type.id=q.question_type_id\n" +
            "AND question_level.level='OPENING'\n" +
            "AND question_type.name='MULTI_CHOICE' ORDER BY RANDOM() LIMIT ?1)\n" +
            "UNION ALL\n" +
            "(SELECT * FROM question q, question_level, question_type\n" +
            "WHERE q.question_level_id=question_level.id\n" +
            "AND question_type.id=q.question_type_id\n" +
            "AND question_level.level='MIDDLE'\n" +
            "AND question_type.name='MULTI_CHOICE' ORDER BY RANDOM() LIMIT ?2)\n" +
            "UNION ALL\n" +
            "(SELECT * FROM question q, question_level, question_type\n" +
            "WHERE q.question_level_id=question_level.id\n" +
            "AND question_type.id=q.question_type_id\n" +
            "AND question_level.level='ENDGAME'\n" +
            "AND question_type.name='MULTI_CHOICE' ORDER BY RANDOM() LIMIT ?3);", nativeQuery = true)
    fun getRandomQuestionsAllLevel(numOfOpening: Int, numOfMiddle: Int, numOfEndGame: Int): MutableList<Question>
}