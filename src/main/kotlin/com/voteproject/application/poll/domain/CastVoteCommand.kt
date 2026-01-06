package com.voteproject.application.poll.command

import java.time.LocalDateTime

data class CreatePollCommand(
    val title: String,
    val description: String? = null,
    val closesAt: LocalDateTime? = null,
    val options: List<String>
)
