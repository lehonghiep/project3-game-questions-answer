package com.hust.project3.gamequestionsanswer.database.repository

import com.hust.project3.gamequestionsanswer.database.entity.Account
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Repository
interface AccountRepository : PagingAndSortingRepository<Account, UUID> {

    fun findByPhoneNumber(phoneNumber: String): Account

    fun existsAccountByPhoneNumber(phoneNumber: String): Boolean

}