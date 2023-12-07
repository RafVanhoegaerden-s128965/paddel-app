package com.example.paddel_app.model

import com.example.paddel_app.enum.Days
import com.example.paddel_app.enum.CourtPosition
import com.example.paddel_app.enum.Hand
import com.example.paddel_app.enum.MatchType
import com.example.paddel_app.enum.PreferredTime

data class Court(
    var id: String,
    val name: String,
    val price: String,
    val address: String,
    val openClosedHours: String,
    val closedDays: List<Days>,
) {
    // No-argument constructor
    constructor() : this("", "", "", "", "", emptyList())
}
