package com.voteproject.domain.poll

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class VoterIdTest {

    @Test
    fun `voterId는 빈 값이면 예외`() {
        assertThrows(IllegalArgumentException::class.java) {
            VoterId("   ")
        }
    }
}
