package com.voteproject.domain.poll

import java.util.Optional

interface PollRepository {

    fun save(poll: Poll): Poll

    fun findById(id: Long): Optional<Poll>

    fun findAll(): List<Poll>
}
