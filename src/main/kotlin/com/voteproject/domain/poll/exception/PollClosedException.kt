package com.voteproject.domain.poll.exception

class PollClosedException(
    message: String = "마감된 투표입니다."
) : RuntimeException(message)
