package com.voteproject.application.poll

import com.voteproject.application.poll.command.CastVoteCommand
import com.voteproject.application.poll.command.CreatePollCommand
import com.voteproject.domain.poll.Poll
import com.voteproject.domain.poll.PollRepository
import com.voteproject.domain.poll.VoterId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PollCommandService(
    private val pollRepository: PollRepository
) {

    @Transactional
    fun create(command: CreatePollCommand): Long {
        val poll = Poll.create(
            title = command.title,
            description = command.description,
            closesAt = command.closesAt,
            optionTexts = command.options
        )
        return pollRepository.save(poll).id!!
    }

    @Transactional
    fun castVote(command: CastVoteCommand, now: LocalDateTime = LocalDateTime.now()) {
        val poll = pollRepository.findById(command.pollId)
            .orElseThrow { IllegalArgumentException("poll not found. id=${command.pollId}") }

        poll.castVote(
            voterId = VoterId(command.voterId),
            optionId = command.optionId,
            now = now
        )
        // Aggregate 수정이므로 save 호출 없어도 JPA dirty checking으로 반영됨
    }

    @Transactional
    fun close(pollId: Long) {
        val poll = pollRepository.findById(pollId)
            .orElseThrow { IllegalArgumentException("poll not found. id=$pollId") }
        poll.close()
    }
}
