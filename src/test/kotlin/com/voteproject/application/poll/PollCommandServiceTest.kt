package com.voteproject.application.poll

import com.voteproject.application.poll.command.CastVoteCommand
import com.voteproject.application.poll.command.CreatePollCommand
import com.voteproject.domain.poll.Poll
import com.voteproject.domain.poll.PollRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Optional

class PollCommandServiceTest {

    @Test
    fun `create - pollId 반환`() {
        val repo = InMemoryPollRepository()
        val service = PollCommandService(repo)

        val id = service.create(
            CreatePollCommand(
                title = "점심 뭐먹지?",
                options = listOf("국밥", "초밥")
            )
        )

        assertEquals(1L, id)
        assertEquals(1, repo.findAll().size)
    }

    @Test
    fun `castVote - 정상 투표`() {
        val repo = InMemoryPollRepository()
        val service = PollCommandService(repo)

        val pollId = service.create(
            CreatePollCommand(
                title = "점심 뭐먹지?",
                options = listOf("국밥", "초밥")
            )
        )

        val poll = repo.findById(pollId).get()
        val optionId = poll.options.first().id!!

        service.castVote(
            CastVoteCommand(
                pollId = pollId,
                voterId = "user1",
                optionId = optionId
            )
        )

        assertEquals(1, repo.findById(pollId).get().votes.size)
    }

    private class InMemoryPollRepository : PollRepository {
        private val store = linkedMapOf<Long, Poll>()
        private var seq = 0L
        private var optionSeq = 0L

        override fun save(poll: Poll): Poll {
            if (poll.id == null) poll.id = ++seq

            // JPA가 해주는 id 부여를 테스트용으로 흉내 (optionId가 필요해서)
            poll.options.forEach { opt ->
                if (opt.id == null) opt.id = ++optionSeq
            }

            store[poll.id!!] = poll
            return poll
        }

        override fun findById(id: Long): Optional<Poll> = Optional.ofNullable(store[id])

        override fun findAll(): List<Poll> = store.values.toList()
    }
}
