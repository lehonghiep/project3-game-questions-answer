package com.hust.project3.gamequestionsanswer.base

import org.hibernate.annotations.Type
import java.time.Clock
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @Type(type = "pg-uuid")
    var id: UUID = UUID.randomUUID()

    @Column(name = "created_date")
    var createdDate: LocalDateTime = LocalDateTime.now(Clock.systemUTC())

    @Column(name = "updated_date")
    var updatedDate: LocalDateTime = LocalDateTime.now(Clock.systemUTC())

    @PrePersist
    fun prePersist() {
        updatedDate = LocalDateTime.now(Clock.systemUTC())
    }

    @PreUpdate
    fun preUpdate() {
        updatedDate = LocalDateTime.now(Clock.systemUTC())
    }

}