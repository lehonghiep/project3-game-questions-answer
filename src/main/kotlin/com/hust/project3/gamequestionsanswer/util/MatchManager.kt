package com.elomath.pro.util

import com.elomath.pro.constant.MatchConstant
import com.elomath.pro.constant.MessageKey
import com.elomath.pro.database.entity.Match
import com.elomath.pro.database.model.Player
import com.elomath.pro.database.model.PlayerAnswer
import com.elomath.pro.database.model.QuestionInMatch
import com.elomath.pro.dto.*
import com.elomath.pro.service.QuestionService
import com.elomath.pro.service.impl.IActionMatchManager
import org.springframework.messaging.simp.SimpMessageSendingOperations
import java.util.*


class MatchManager(var simpMessageSendingOperations: SimpMessageSendingOperations,
                   var questionService: QuestionService,
                   var simpleBrokerPrefix: String,
                   var iActionMatchManager: IActionMatchManager,
                   var roomId: UUID, var playerOneId: UUID, var playerTwoId: UUID,
                   var eloPlayerOne: Int, var eloPlayerTwo: Int) {

    var listPlayer: MutableMap<UUID, Player> = mutableMapOf()

    lateinit var listQuestions: MutableList<QuestionDto>

    var currentQuestion: QuestionInMatch? = null

    lateinit var countDownTimer: CountDownTimer

    var firstPlusPoint: Int? = null
    var secondPlusPoint: Int? = null
    var minusPoint: Int? = null
    var countDownTime: Long? = null

    var playerIsGiveUp: Boolean = false

    fun begin() {
        addPlayers()
        queryQuestions()
        startMatch()
    }

    private fun startMatch() {
        initMatchParam(MatchConstant.OPENING_FIRST_PLUS_POINT,
                MatchConstant.OPENING_SECOND_PLUS_POINT,
                MatchConstant.OPENING_MINUS_POINT,
                MatchConstant.COUNT_DOWN_TIME_OPENING)
        delayTimeBeforeSendQuestion()
    }

    private fun initMatchParam(firstPlusPoint: Int, secondPlusPoint: Int,
                               pointMinus: Int, countDownTime: Long) {
        this.firstPlusPoint = firstPlusPoint
        this.secondPlusPoint = secondPlusPoint
        this.minusPoint = pointMinus
        this.countDownTime = countDownTime
    }

    private fun queryQuestions() {
        listQuestions = questionService.getRandomQuestionsAllLevel(MatchConstant.NUMBER_OF_OPENING_QUESTIONS,
                MatchConstant.NUMBER_OF_MIDDLE_QUESTIONS,
                MatchConstant.NUMBER_OF_ENDGAME_QUESTIONS)
    }

    private fun addPlayers() {
        listPlayer[playerOneId] = Player(playerOneId,
                0)
        listPlayer[playerTwoId] = Player(playerTwoId,
                0)
    }

    private fun sendQuestionAndCountDown() {
        if (!listQuestions.isEmpty()) {
            val question = listQuestions.removeAt(0)

            val listPlayersAnswer: MutableList<PlayerAnswer> = mutableListOf()
            currentQuestion = QuestionInMatch(question.orderNumber,
                    question.question,
                    question.answers,
                    question.trueAnswer,
                    question.questionType,
                    question.questionLevel,
                    listPlayersAnswer)

            sendTo(roomId, ResponseDto<QuestionDto>(question, StatusDto(MessageKey.SUCCESS)))
            countDownTimer = CountDownTimer(countDownTime!!, 1, object : ICountDownAction {
                override fun afterPeriodTime(currentTime: Long, iActionStopCountDown: IActionStopCountDown) {
                    if (currentQuestion!!.listPlayersAnswer.size > 1) {
                        iActionStopCountDown.cancel()
                        return
                    }
                    sendTo(roomId, "/count-down", currentTime)
                }

                override fun afterFinish() {
                    if (!handleCheckPlayerNotRequest()) {
                        return
                    }
                    checkSwitchLevel()
                    //sendQuestionAndCountDown()
                    delayTimeBeforeSendQuestion()
                }
            })
            countDownTimer.run()
        } else {
            finishMatch()
        }
    }

    private fun delayTimeBeforeSendQuestion() {
        var delayTime: Long
        if (currentQuestion != null) {
            val currentOrderNumber = currentQuestion!!.orderNumber
            if (currentOrderNumber == MatchConstant.NUMBER_OF_OPENING_QUESTIONS
                    || currentOrderNumber == (MatchConstant.NUMBER_OF_OPENING_QUESTIONS +
                            MatchConstant.NUMBER_OF_MIDDLE_QUESTIONS)) {
                delayTime = 3L
            } else if (currentOrderNumber == 18) {
                delayTime = 0L
            } else {
                delayTime = 1L
            }
        } else {
            delayTime = 1L
        }

        countDownTimer = CountDownTimer(delayTime, 1, object : ICountDownAction {
            override fun afterPeriodTime(currentTime: Long, iActionStopCountDown: IActionStopCountDown) {

            }

            override fun afterFinish() {
                sendQuestionAndCountDown()
            }

        })
        countDownTimer.run()
    }

    private fun handleCheckPlayerNotRequest(): Boolean {
        var playerOne = listPlayer[playerOneId]
        var playerTwo = listPlayer[playerTwoId]

        setNumOfPlayerNotRequest(playerOne, playerTwo)

        return checkIsMatchIsContinuable(playerOne, playerTwo)

    }

    private fun checkIsMatchIsContinuable(playerOne: Player?, playerTwo: Player?): Boolean {
        val playerOneIsContinuable = playerOne!!.checkAcceptContinuePlay()
        val playerTwoIsContinuable = playerTwo!!.checkAcceptContinuePlay()

        if (playerOneIsContinuable && playerTwoIsContinuable) {
            //match continue
            return true
        } else if (playerOneIsContinuable) {
            //player 1 win
            cancelMatch(playerOne, playerTwo, playerOne.accountId)
        } else if (playerTwoIsContinuable) {
            //player 2 win
            cancelMatch(playerOne, playerTwo, playerTwo.accountId)
        } else {
            //draw
            cancelMatch(playerOne, playerTwo, null)
        }
        return false
    }

    private fun cancelMatch(playerOne: Player, playerTwo: Player, winner: UUID?) {
        countDownTimer.cancel()
        var listElo: MutableList<Int>? = null
        when {
            winner == null -> {
                listElo = EloCalculatorUtil.scoreElo(eloPlayerOne, eloPlayerTwo, MatchConstant.PLAYER_DRAW)
            }
            winner == playerOneId -> {
                listElo = EloCalculatorUtil.scoreElo(eloPlayerOne, eloPlayerTwo, MatchConstant.PLAYER_WIN)
            }
            else -> {
                listElo = EloCalculatorUtil.scoreElo(eloPlayerOne, eloPlayerTwo, MatchConstant.PLAYER_LOSE)
            }
        }
        val listResultPlayerDto: MutableList<ResultPlayerDto> = mutableListOf()

        listResultPlayerDto.add(ResultPlayerDto(playerOneId, playerOne!!.score, listElo[0]))
        listResultPlayerDto.add(ResultPlayerDto(playerTwoId, playerTwo!!.score, listElo[1]))

        saveMatchResult(playerOne, playerTwo, winner, listElo[0], listElo[1])
        sendTo(roomId, ResponseDto(MatchResultDto(
                listResultPlayerDto,
                winner),
                StatusDto(MessageKey.MATCH_CANCELED_BY_PLAYER_NOT_REQUEST)))

    }

    private fun setNumOfPlayerNotRequest(playerOne: Player?, playerTwo: Player?) {
        val listPlayerAnswer = currentQuestion!!.listPlayersAnswer
        if (listPlayerAnswer.size > 1) {
            playerOne!!.resetNumOfQuestionNotAnswer()
            playerTwo!!.resetNumOfQuestionNotAnswer()
        } else if (listPlayerAnswer.size > 0) {
            if (listPlayerAnswer[0].accountId == playerOneId) {
                playerOne!!.resetNumOfQuestionNotAnswer()
                playerTwo!!.increaseNumOfQuestionNotAnswer()
            } else {
                playerTwo!!.resetNumOfQuestionNotAnswer()
                playerOne!!.increaseNumOfQuestionNotAnswer()
            }
        } else {
            playerOne!!.increaseNumOfQuestionNotAnswer()
            playerTwo!!.increaseNumOfQuestionNotAnswer()
        }
    }

    private fun finishMatch() {
        val playerOne = listPlayer[playerOneId]
        val playerTwo = listPlayer[playerTwoId]

        setAndSendMatchResult(playerOne, playerTwo)
    }

    private fun setAndSendMatchResult(playerOne: Player?, playerTwo: Player?) {
        lateinit var matchResult: String
        var winner: UUID? = null
        when {
            playerOne!!.score > playerTwo!!.score -> {
                matchResult = MatchConstant.PLAYER_WIN
                winner = playerOneId
            }
            playerOne.score < playerTwo.score -> {
                matchResult = MatchConstant.PLAYER_LOSE
                winner = playerTwoId
            }
            else -> matchResult = MatchConstant.PLAYER_DRAW
        }

        val listElo = EloCalculatorUtil.scoreElo(eloPlayerOne, eloPlayerTwo, matchResult)

        val listResultPlayerDto: MutableList<ResultPlayerDto> = mutableListOf()

        listResultPlayerDto.add(ResultPlayerDto(playerOneId, playerOne!!.score, listElo[0]))
        listResultPlayerDto.add(ResultPlayerDto(playerTwoId, playerTwo!!.score, listElo[1]))

        saveMatchResult(playerOne, playerTwo, winner, listElo[0], listElo[1])

        sendTo(roomId, ResponseDto(MatchResultDto(
                listResultPlayerDto,
                winner),
                StatusDto(MessageKey.MATCH_FINISH)))

    }

    private fun checkSwitchLevel() {
        when {
            currentQuestion!!.orderNumber == MatchConstant.NUMBER_OF_OPENING_QUESTIONS -> {
                sendTo(roomId, "/message", ResponseDto<Any>(null, StatusDto(MessageKey.SWITCH_LEVEL_TO_MIDDLE)))
                initMatchParam(MatchConstant.MIDDLE_FIRST_PLUS_POINT,
                        MatchConstant.MIDDLE_SECOND_PLUS_POINT,
                        MatchConstant.MIDDLE_MINUS_POINT,
                        MatchConstant.COUNT_DOWN_TIME_MIDDLE)

            }
            currentQuestion!!.orderNumber == (MatchConstant.NUMBER_OF_OPENING_QUESTIONS
                    + MatchConstant.NUMBER_OF_MIDDLE_QUESTIONS) -> {
                sendTo(roomId, "/message", ResponseDto<Any>(null, StatusDto(MessageKey.SWITCH_LEVEL_TO_ENDGAME)))
                initMatchParam(MatchConstant.ENDGAME_FIRST_PLUS_POINT,
                        MatchConstant.ENDGAME_SECOND_PLUS_POINT,
                        MatchConstant.ENDGAME_MINUS_POINT,
                        MatchConstant.COUNT_DOWN_TIME_ENDGAME)

            }

        }
    }

    fun receiveAnswer(accountId: UUID, answerDto: AnswerDto) {
        if (currentQuestion!!.orderNumber != answerDto.orderNumber) {
            return
        }

        handleProcessingAnswer(answerDto, accountId)
    }

    private fun handleProcessingAnswer(answerDto: AnswerDto, accountId: UUID) {
        val player = listPlayer[accountId]
        val listPlayerAnswer = currentQuestion!!.listPlayersAnswer
        synchronized(this) {
            for (item in listPlayerAnswer) {
                if (item.accountId == accountId) {
                    return
                }
            }

            var isCorrect = false
            if (currentQuestion!!.trueAnswer == answerDto.answer) {
                isCorrect = true
            }
            listPlayerAnswer.add(PlayerAnswer(accountId, isCorrect))
            updateScore(player!!)
            sendTo(roomId, "/answer", ResponseDto(ResultAnswerPlayerDto(player.accountId,
                    answerDto.orderNumber, isCorrect, player.score), StatusDto(MessageKey.SEND_RESULT_ANSWER_TO_PLAYER)))
        }
    }

    private fun updateScore(player: Player) {
        var score = player.score
        val listPlayerAnswer = currentQuestion!!.listPlayersAnswer
        if (listPlayerAnswer.size == 1) {
            val playerAnswer = listPlayerAnswer[0]
            if (playerAnswer.isCorrect) {
                score += firstPlusPoint!!
            } else {
                score = max(0, score - minusPoint!!)
            }
        } else if (listPlayerAnswer.size == 2) {
            val playerAnswerOne = listPlayerAnswer[0]
            val playerAnswerTwo = listPlayerAnswer[1]
            if (playerAnswerOne.isCorrect && playerAnswerTwo.isCorrect) {
                score += secondPlusPoint!!
            } else if (!playerAnswerOne.isCorrect && playerAnswerTwo.isCorrect) {
                score += firstPlusPoint!!
            } else {
                score = max(0, score - minusPoint!!)
            }
        }
        player.score = score
    }

    private fun max(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    fun playerGiveUp(accountId: UUID) {
        val playerOne = listPlayer[playerOneId]
        val playerTwo = listPlayer[playerTwoId]

        synchronized(this) {
            if (!playerIsGiveUp) {
                playerIsGiveUp = true
            } else {
                return
            }
            var winner: UUID? = null
            countDownTimer.cancel()
            lateinit var matchResult: String
            if (accountId == playerOneId) {
                matchResult = MatchConstant.PLAYER_LOSE
                winner = playerTwoId
            } else {
                matchResult = MatchConstant.PLAYER_WIN
                winner = playerOneId
            }

            val listElo = EloCalculatorUtil.scoreElo(eloPlayerOne, eloPlayerTwo, matchResult)

            val listResultPlayerDto: MutableList<ResultPlayerDto> = mutableListOf()

            listResultPlayerDto.add(ResultPlayerDto(playerOneId, playerOne!!.score, listElo[0]))
            listResultPlayerDto.add(ResultPlayerDto(playerTwoId, playerTwo!!.score, listElo[1]))

            saveMatchResult(playerOne, playerTwo, winner, listElo[0], listElo[1])

            sendTo(roomId, ResponseDto(MatchResultDto(
                    listResultPlayerDto,
                    winner),
                    StatusDto(MessageKey.PLAYER_REQUIRE_LOSE)))

        }
    }

    private fun saveMatchResult(playerOne: Player, playerTwo: Player, winner: UUID?,
                                eloPlayerOne: Int, eloPlayerTwo: Int) {
        var match: Match = Match()

        match.id = roomId
        match.playerOne = playerOneId
        match.playerTwo = playerTwoId
        match.playerOneScore = playerOne.score
        match.playerTwoScore = playerTwo.score
        match.winner = winner

        iActionMatchManager.saveMatchResult(match, eloPlayerOne, eloPlayerTwo)
        iActionMatchManager.removeMatchManager(roomId)
    }

    private fun sendTo(path: String, payload: Any) {
        simpMessageSendingOperations.convertAndSend("$simpleBrokerPrefix/$path", payload)
    }

    private fun sendTo(roomId: UUID, prefix: String, payload: Any) {
        sendTo(roomId.toString() + prefix, payload)
    }

    private fun sendTo(path: UUID, payload: Any) {
        sendTo(path.toString(), payload)
    }
}