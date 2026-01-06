package com.voteproject.application.poll

import com.voteproject.domain.poll.Poll
import com.voteproject.domain.poll.PollRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PollQueryService(
    private val pollRepository: PollRepository
) {

    @Transactional(readOnly = true)
    fun get(pollId: Long): Poll =
        pollRepository.findById(pollId)
            .orElseThrow { IllegalArgumentException("poll not found. id=$pollId") }

    @Transactional(readOnly = true)
    fun list(): List<Poll> = pollRepository.findAll()

    /**
     * 결과: optionId -> voteCount
     */
    @Transactional(readOnly = true)
    fun results(pollId: Long): Map<Long, Long> {
        val poll = get(pollId)
        val counts = mutableMapOf<Long, Long>()
        poll.options.forEach { opt ->
            val optId = opt.id ?: return@forEach
            counts[optId] = poll.votes.count { it.option.id == optId }.toLong()
        }
        return counts
    }
}
