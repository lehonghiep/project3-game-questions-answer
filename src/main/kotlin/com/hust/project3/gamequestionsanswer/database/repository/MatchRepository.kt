package com.hust.project3.gamequestionsanswer.database.repository

import com.hust.project3.gamequestionsanswer.database.entity.Match
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@Repository
interface MatchRepository : PagingAndSortingRepository<Match, UUID> {

}