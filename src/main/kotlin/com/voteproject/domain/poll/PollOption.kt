package com.voteproject.domain.poll

import jakarta.persistence.*

@Entity
@Table(name = "poll_option")
open class PollOption protected constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    open var poll: Poll,

    @Column(nullable = false, length = 100)
    open var text: String
) {

    companion object {
        fun create(poll: Poll, text: String): PollOption {
            require(text.isNotBlank()) { "option text must not be blank" }
            return PollOption(
                poll = poll,
                text = text.trim()
            )
        }
    }
}
