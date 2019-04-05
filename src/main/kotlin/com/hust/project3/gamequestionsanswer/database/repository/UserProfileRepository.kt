package com.hust.project3.gamequestionsanswer.database.repository

import com.hust.project3.gamequestionsanswer.database.entity.UserProfile
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Repository
interface UserProfileRepository : PagingAndSortingRepository<UserProfile, UUID> {
    @Modifying
    @Query("UPDATE user_profile up " +
            "SET elo = ?1 " +
            "FROM account a " +
            "WHERE up.id = a.user_profile_id AND a.id = ?2 ", nativeQuery = true)
    fun updateEloByAccountId(elo: Int, accountId: UUID)
}