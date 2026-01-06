package com.voteproject.domain.poll

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "vote",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_vote_poll_voter",
            columnNames = ["poll_id", "voter_id"]
        )
    ]
)
open class Vote protected constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    open var poll: Poll,

    @Embedded
    open var voterId: VoterId,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    open var option: PollOption,

    @Column(nullable = false)
    open var votedAt: LocalDateTime
) {

    companion object {
        fun create(
            poll: Poll,
            voterId: VoterId,
            option: PollOption,
            votedAt: LocalDateTime
        ): Vote {
            return Vote(
                poll = poll,
                voterId = voterId,
                option = option,
                votedAt = votedAt
            )
        }
    }
}
