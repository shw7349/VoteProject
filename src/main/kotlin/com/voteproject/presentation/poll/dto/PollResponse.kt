package com.voteproject.presentation.poll.dto

import com.voteproject.domain.poll.Poll
import com.voteproject.domain.poll.PollStatus
import java.time.LocalDateTime

data class PollResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: PollStatus,
    val closesAt: LocalDateTime?,
    val options: List<PollOptionResponse>
) {
    companion object {
        fun from(poll: Poll): PollResponse {
            return PollResponse(
                id = poll.id!!,
                title = poll.title,
                description = poll.description,
                status = poll.status,
                closesAt = poll.closesAt,
                options = poll.options.map { PollOptionResponse(it.id!!, it.text) }
            )
        }
    }
}

data class PollOptionResponse(
    val id: Long,
    val text: String
)
