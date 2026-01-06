package com.voteproject.domain.poll.exception

class OptionNotFoundException(
    optionId: Long,
    message: String = "존재하지 않는 선택지입니다. optionId=$optionId"
) : RuntimeException(message)
