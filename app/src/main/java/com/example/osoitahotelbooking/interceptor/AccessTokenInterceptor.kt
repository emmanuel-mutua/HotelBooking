package com.example.osoitahotelbooking.interceptor


import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AccessTokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val keys = "okFGlndKNWRMKVkIUmWrJAITNFDBWnec7cyjnSyASY1qxVCP:WrzhQZPtaK7VzznkOZBmKEGT3AZlNdKQIWfpP3grYHVMguvvYTGNI4x66UhZYrmv"
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Basic " + Base64.encodeToString(keys.toByteArray(), Base64.NO_WRAP))
            .build()
        return chain.proceed(request)
    }
}
