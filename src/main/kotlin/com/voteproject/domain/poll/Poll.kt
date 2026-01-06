package com.voteproject.domain.poll

import com.voteproject.domain.poll.exception.AlreadyVotedException
import com.voteproject.domain.poll.exception.OptionNotFoundException
import com.voteproject.domain.poll.exception.PollClosedException
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "poll")
open class Poll protected constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(nullable = false, length = 100)
    open var title: String,

    @Column(nullable = true, length = 500)
    open var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    open var status: PollStatus = PollStatus.OPEN,

    /**
     * null 이면 무기한 OPEN
     */
    @Column(nullable = true)
    open var closesAt: LocalDateTime? = null,

    @OneToMany(
        mappedBy = "poll",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    open val options: MutableList<PollOption> = mutableListOf(),

    @OneToMany(
        mappedBy = "poll",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    open val votes: MutableList<Vote> = mutableListOf(),

    @Version
    open var version: Long? = null
) {

    companion object {
        fun create(
            title: String,
            description: String? = null,
            closesAt: LocalDateTime? = null,
            optionTexts: List<String>
        ): Poll {
            require(title.isNotBlank()) { "title must not be blank" }
            require(optionTexts.size >= 2) { "poll must have at least 2 options" }

            val poll = Poll(
                title = title.trim(),
                description = description?.trim(),
                status = PollStatus.OPEN,
                closesAt = closesAt
            )

            optionTexts
                .map { it.trim() }
                .forEach { text ->
                    require(text.isNotBlank()) { "option text must not be blank" }
                    poll.addOption(text)
                }

            return poll
        }
    }

    /**
     * 투표 (1인 1표, 마감 이후 불가, 존재하지 않는 옵션 불가)
     */
    open fun castVote(voterId: VoterId, optionId: Long, now: LocalDateTime = LocalDateTime.now()) {
        refreshStatusIfExpired(now)

        if (status == PollStatus.CLOSED) throw PollClosedException()
        if (hasVoted(voterId)) throw AlreadyVotedException()

        val option = options.firstOrNull { it.id == optionId } ?: throw OptionNotFoundException(optionId)

        // Vote 엔티티는 (poll, voterId, optionId) 정보를 들고 있다고 가정
        votes.add(Vote.create(poll = this, voterId = voterId, option = option, votedAt = now))
    }

    open fun close() {
        status = PollStatus.CLOSED
    }

    open fun refreshStatusIfExpired(now: LocalDateTime = LocalDateTime.now()) {
        val deadline = closesAt ?: return
        if (status == PollStatus.OPEN && !now.isBefore(deadline)) {
            status = PollStatus.CLOSED
        }
    }

    open fun hasVoted(voterId: VoterId): Boolean =
        votes.any { it.voterId == voterId }

    private fun addOption(text: String) {
        options.add(PollOption.create(poll = this, text = text))
    }
}
