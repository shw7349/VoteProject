package com.voteproject.infrastructure.persistence

import com.voteproject.domain.poll.Poll
import com.voteproject.domain.poll.PollRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class PollRepositoryAdapter(
    private val jpa: SpringDataPollJpa
) : PollRepository {

    override fun save(poll: Poll): Poll = jpa.save(poll)

    override fun findById(id: Long): Optional<Poll> = jpa.findById(id)

    override fun findAll(): List<Poll> = jpa.findAll()
}
