package com.voteproject.application.poll.command

data class CastVoteCommand(
    val pollId: Long,
    val voterId: String,
    val optionId: Long
)
