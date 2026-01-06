package com.voteproject.domain.poll

import com.voteproject.domain.poll.exception.AlreadyVotedException
import com.voteproject.domain.poll.exception.OptionNotFoundException
import com.voteproject.domain.poll.exception.PollClosedException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PollTest {

    @Test
    fun `투표 생성 - 옵션 2개 미만이면 예외`() {
        assertThrows(IllegalArgumentException::class.java) {
            Poll.create(
                title = "점심 뭐먹지?",
                optionTexts = listOf("국밥")
            )
        }
    }

    @Test
    fun `투표 - 마감된 투표는 투표 불가`() {
        val poll = Poll.create(
            title = "점심 뭐먹지?",
            optionTexts = listOf("국밥", "초밥"),
            closesAt = LocalDateTime.now().minusMinutes(1)
        )
        // 도메인 테스트에서는 JPA가 id를 안 넣어주므로 직접 세팅
        poll.options[0].id = 1L
        poll.options[1].id = 2L

        assertThrows(PollClosedException::class.java) {
            poll.castVote(VoterId("user1"), optionId = 1L, now = LocalDateTime.now())
        }
    }

    @Test
    fun `투표 - 존재하지 않는 optionId는 예외`() {
        val poll = Poll.create(
            title = "점심 뭐먹지?",
            optionTexts = listOf("국밥", "초밥")
        )
        poll.options[0].id = 1L
        poll.options[1].id = 2L

        assertThrows(OptionNotFoundException::class.java) {
            poll.castVote(VoterId("user1"), optionId = 999L, now = LocalDateTime.now())
        }
    }

    @Test
    fun `투표 - 1인 1표 보장`() {
        val poll = Poll.create(
            title = "점심 뭐먹지?",
            optionTexts = listOf("국밥", "초밥")
        )
        poll.options[0].id = 1L
        poll.options[1].id = 2L

        poll.castVote(VoterId("user1"), optionId = 1L, now = LocalDateTime.now())

        assertThrows(AlreadyVotedException::class.java) {
            poll.castVote(VoterId("user1"), optionId = 2L, now = LocalDateTime.now())
        }
    }

    @Test
    fun `투표 - 정상 투표 시 vote가 쌓인다`() {
        val poll = Poll.create(
            title = "점심 뭐먹지?",
            optionTexts = listOf("국밥", "초밥")
        )
        poll.options[0].id = 1L
        poll.options[1].id = 2L

        poll.castVote(VoterId("user1"), optionId = 2L, now = LocalDateTime.now())

        assertEquals(1, poll.votes.size)
        assertEquals("user1", poll.votes[0].voterId.value)
        assertEquals(2L, poll.votes[0].option.id)
    }
}
