package com.voteproject.presentation.poll

import com.voteproject.application.poll.PollCommandService
import com.voteproject.application.poll.PollQueryService
import com.voteproject.application.poll.command.CastVoteCommand
import com.voteproject.application.poll.command.CreatePollCommand
import com.voteproject.presentation.poll.dto.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/polls")
class PollController(
    private val commandService: PollCommandService,
    private val queryService: PollQueryService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: CreatePollRequest): CreatePollResponse {
        val pollId = commandService.create(
            CreatePollCommand(
                title = req.title,
                description = req.description,
                closesAt = req.closesAt,
                options = req.options
            )
        )
        return CreatePollResponse(pollId)
    }

    @GetMapping
    fun list(): List<PollResponse> =
        queryService.list().map { PollResponse.from(it) }

    @GetMapping("/{pollId}")
    fun get(@PathVariable pollId: Long): PollResponse =
        PollResponse.from(queryService.get(pollId))

    @PostMapping("/{pollId}/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun vote(@PathVariable pollId: Long, @RequestBody req: CastVoteRequest) {
        commandService.castVote(
            CastVoteCommand(
                pollId = pollId,
                voterId = req.voterId,
                optionId = req.optionId
            )
        )
    }

    @GetMapping("/{pollId}/results")
    fun results(@PathVariable pollId: Long): PollResultResponse {
        val map = queryService.results(pollId)
        return PollResultResponse(
            pollId = pollId,
            results = map.entries
                .sortedBy { it.key }
                .map { OptionResult(optionId = it.key, count = it.value) }
        )
    }
}
