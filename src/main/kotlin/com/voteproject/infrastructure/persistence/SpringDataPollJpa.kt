package com.voteproject.infrastructure.persistence

import com.voteproject.domain.poll.Poll
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataPollJpa : JpaRepository<Poll, Long>
