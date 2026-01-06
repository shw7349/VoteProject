package com.voteproject.presentation.poll.dto

data class PollResultResponse(
    val pollId: Long,
    val results: List<OptionResult>
)

data class OptionResult(
    val optionId: Long,
    val count: Long
)
