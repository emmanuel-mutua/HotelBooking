package com.example.osoitahotelbooking.services


import com.example.osoitahotelbooking.payment.AccessToken
import com.example.osoitahotelbooking.payment.STKPush
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DarajaApiService {
    @POST("mpesa/stkpush/v1/processrequest")
    fun sendPush(@Body stkPush: STKPush): Call<STKPush>

    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun getAccessToken(): Call<AccessToken>
}
