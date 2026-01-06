package com.voteproject.presentation.poll

import com.fasterxml.jackson.databind.ObjectMapper
import com.voteproject.application.poll.PollCommandService
import com.voteproject.application.poll.PollQueryService
import com.voteproject.presentation.poll.dto.CreatePollRequest
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(PollController::class)
class PollControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var commandService: PollCommandService

    @MockitoBean
    lateinit var queryService: PollQueryService

    @Test
    fun `POST polls - 생성 성공 201`() {
        `when`(commandService.create(any())).thenReturn(1L)

        val req = CreatePollRequest(
            title = "점심 뭐먹지?",
            options = listOf("국밥", "초밥")
        )

        mockMvc.post("/polls") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(req)
        }
            .andExpect {
                status { isCreated() }
                jsonPath("$.pollId") { value(1) }
            }
    }
}
