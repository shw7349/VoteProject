package com.voteproject.domain.poll

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VoteTest {

    @Test
    fun `Vote create - 필드가 정상 세팅된다`() {
        val poll = Poll.create("테스트", optionTexts = listOf("A", "B"))
        poll.options[0].id = 1L
        poll.options[1].id = 2L

        val now = LocalDateTime.now()
        val vote = Vote.create(
            poll = poll,
            voterId = VoterId("u1"),
            option = poll.options[0],
            votedAt = now
        )

        assertEquals("u1", vote.voterId.value)
        assertEquals(1L, vote.option.id)
        assertEquals(now, vote.votedAt)
    }
}
