package com.voteproject.presentation.poll.dto

import java.time.LocalDateTime

data class CreatePollRequest(
    val title: String,
    val description: String? = null,
    val closesAt: LocalDateTime? = null,
    val options: List<String>
)
