package com.example.expensemanager.models

data class SeatBookRequestModel(
    var hall_id: Int,
    var seat_id: Int,
    var slot_id: Int,
    var booked_by: String,
    var booking_date: String
)