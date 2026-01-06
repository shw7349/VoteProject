package com.voteproject.domain.poll.exception

class AlreadyVotedException(
    message: String = "이미 투표한 사용자입니다."
) : RuntimeException(message)
