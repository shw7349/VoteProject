package com.voteproject.presentation.poll.dto

data class CastVoteRequest(
    val voterId: String,
    val optionId: Long
)
