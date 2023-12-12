package com.example.paddel_app.model

data class Booking(
    val id: String,
    val userId: String,
    val courtId: String,
    val startTime: String,
    val endTime: String,
){
    // No-argument constructor
    constructor() : this("", "", "", "", "")
}