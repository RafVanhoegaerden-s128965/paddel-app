package com.example.paddel_app.model;

import com.example.paddel_app.enum.*;

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: String,
    val birthDate: String,
    val bestHand: Hand? = null,
    val courtPosition: CourtPosition? = null,
    val matchType: MatchType? = null,
    val preferredTime: PreferredTime? = null
) {
    // No-argument constructor
    constructor() : this("", "", "", "", "", "", null, null, null, null)
}
