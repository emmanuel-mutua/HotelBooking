package com.example.osoitahotelbooking.payment

import com.google.gson.annotations.SerializedName

data class STKPush(
    @SerializedName("BusinessShortCode")
    val businessShortCode: String,

    @SerializedName("Password")
    val password: String,

    @SerializedName("Timestamp")
    val timestamp: String,

    @SerializedName("TransactionType")
    val transactionType: String,

    @SerializedName("Amount")
    val amount: String,  // Store amount as String

    @SerializedName("PartyA")
    val partyA: String,

    @SerializedName("PartyB")
    val partyB: String,

    @SerializedName("PhoneNumber")
    val phoneNumber: String,

    @SerializedName("CallBackURL")
    val callBackURL: String,

    @SerializedName("AccountReference")
    val accountReference: String,

    @SerializedName("TransactionDesc")
    val transactionDesc: String
) {
    constructor(
        businessShortCode: String,
        password: String,
        timestamp: String,
        transactionType: String,
        amount: Int,
        partyA: String,
        partyB: String,
        phoneNumber: String,
        callBackURL: String,
        accountReference: String,
        transactionDesc: String
    ) : this(
        businessShortCode,
        password,
        timestamp,
        transactionType,
        amount.toString(),  // Convert Int to String
        partyA,
        partyB,
        phoneNumber,
        callBackURL,
        accountReference,
        transactionDesc
    )
}
