package com.example.osoitahotelbooking.payment

data class STKPushResponse(
    val merchantRequestID: String,
    val checkoutRequestID: String,
    val responseCode: String,
    val responseDescription: String,
    val customerMessage: String
)
