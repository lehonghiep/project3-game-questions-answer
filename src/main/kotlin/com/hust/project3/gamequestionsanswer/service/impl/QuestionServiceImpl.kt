package com.elomath.pro.service.impl

import com.elomath.pro.database.entity.Question
import com.elomath.pro.database.repository.QuestionRepository
import com.elomath.pro.dto.QuestionDto
import com.elomath.pro.service.QuestionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class QuestionServiceImpl : QuestionService {
    @Autowired
    lateinit var questionRepository: QuestionRepository

    override fun getRandomQuestionsByQuestionLevel(questionLevel: String, numOfQuestion: Int): MutableList<QuestionDto> {
        var listQuestion = questionRepository.getRandomQuestionsByQuestionLevel(questionLevel, numOfQuestion)

        val listQuestionDto = tranferQuestionToQuestionDto(listQuestion)
        return listQuestionDto
    }

    private fun tranferQuestionToQuestionDto(listQuestion: MutableList<Question>): MutableList<QuestionDto> {
        val listQuestionDto = mutableListOf<QuestionDto>()

        var orderMumber = 1
        for (question in listQuestion) {
            var questionDto = QuestionDto(
                    orderMumber,
                    question.question,
                    question.answers,
                    question.trueAnswer,
                    question.questionType!!.name,
                    question.questionLevel!!.level!!
            )

            listQuestionDto.add(questionDto)
            orderMumber++
        }
        return listQuestionDto
    }

    override fun getRandomQuestionsAllLevel(numOfOpening: Int, numOfMiddle: Int, numOfEndGame: Int): MutableList<QuestionDto> {
        val listQuestion = questionRepository.getRandomQuestionsAllLevel(numOfOpening, numOfMiddle, numOfEndGame)
        val listQuestionDto = tranferQuestionToQuestionDto(listQuestion)
        return listQuestionDto
    }

}