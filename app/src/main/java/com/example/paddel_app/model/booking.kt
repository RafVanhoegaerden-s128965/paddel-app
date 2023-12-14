package com.example.paddel_app.model

data class Booking(
    var id: String,
    val userId: String,
    val courtId: String,
    val startTime: String,
    val endTime: String,
    val date: String
) {
    // No-argument constructor
    constructor() : this("", "", "", "", "", "")
}