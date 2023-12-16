package com.example.paddel_app.model

import com.example.paddel_app.enum.MatchType

data class Game(
    var id: String,
    val userIdOwner: String,
    val userIdPlayer2: String,
    val userIdPlayer3: String,
    val userIdPlayer4: String,
    val bookingId: String,
    val matchType: MatchType? = null
) {
    // No-argument constructor
    constructor() : this("", "", "", "", "", "", null)
}