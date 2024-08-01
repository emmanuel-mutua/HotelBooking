package com.example.osoitahotelbooking.services

import android.os.Build
import com.example.osoitahotelbooking.Constants.BASE_URL
import com.example.osoitahotelbooking.Constants.CONNECT_TIMEOUT
import com.example.osoitahotelbooking.Constants.READ_TIMEOUT
import com.example.osoitahotelbooking.Constants.WRITE_TIMEOUT
import com.example.osoitahotelbooking.interceptor.AccessTokenInterceptor
import com.example.osoitahotelbooking.interceptor.AuthInterceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DarajaApiClient {
    private var retrofit: Retrofit? = null
    private var isDebug: Boolean = false
    private var isGetAccessToken: Boolean = false
    private var mAuthToken: String? = null
    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    fun setIsDebug(isDebug: Boolean): DarajaApiClient {
        this.isDebug = isDebug
        return this
    }

    fun setAuthToken(authToken: String): DarajaApiClient {
        mAuthToken = authToken
        return this
    }

    fun setGetAccessToken(getAccessToken: Boolean): DarajaApiClient {
        isGetAccessToken = getAccessToken
        return this
    }

    private fun okHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient
            .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
        return okHttpClient
    }

    private fun getRestAdapter(): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        if (isDebug) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        val okhttpBuilder = okHttpClient()

        if (isGetAccessToken) {
            okhttpBuilder.addInterceptor(AccessTokenInterceptor())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (!mAuthToken.isNullOrEmpty()) {
                okhttpBuilder.addInterceptor(AuthInterceptor(mAuthToken!!))
            }
        }

        builder.client(okhttpBuilder.build())
        retrofit = builder.build()
        return retrofit!!
    }

    fun mpesaService(): DarajaApiService {
        return getRestAdapter().create(DarajaApiService::class.java)
    }
}
