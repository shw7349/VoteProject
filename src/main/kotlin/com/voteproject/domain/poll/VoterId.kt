package com.voteproject.domain.poll

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class VoterId(

    @Column(name = "voter_id", nullable = false, length = 100)
    val value: String
) {
    init {
        require(value.isNotBlank()) { "voterId must not be blank" }
    }
}
