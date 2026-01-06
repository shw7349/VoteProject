package com.voteproject.infrastructure.persistence

import com.voteproject.domain.poll.Poll
import com.voteproject.domain.poll.VoterId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

@DataJpaTest
class PollRepositoryAdapterTest(
    private val jpa: SpringDataPollJpa
) {

    @Test
    fun `poll 저장 후 조회`() {
        val poll = Poll.create(
            title = "점심 뭐먹지?",
            optionTexts = listOf("국밥", "초밥")
        )

        val saved = jpa.saveAndFlush(poll)

        val found = jpa.findById(saved.id!!).get()
        assertEquals("점심 뭐먹지?", found.title)
        assertEquals(2, found.options.size)
    }

    @Test
    fun `vote 저장 - poll과 함께 cascade로 저장된다`() {
        val poll = Poll.create(
            title = "점심 뭐먹지?",
            optionTexts = listOf("국밥", "초밥")
        )

        val saved = jpa.saveAndFlush(poll)
        val optionId = saved.options.first().id!!

        // 투표
        saved.castVote(
            voterId = VoterId("user1"),
            optionId = optionId,
            now = LocalDateTime.now()
        )

        jpa.saveAndFlush(saved)

        val found = jpa.findById(saved.id!!).get()
        assertEquals(1, found.votes.size)
        assertEquals("user1", found.votes.first().voterId.value)
    }
}
