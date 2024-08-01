package com.example.osoitahotelbooking
import android.util.Base64

object Constants {
    // M-Pesa configuration
    val CONNECT_TIMEOUT: Int = 60 * 1000

    const val READ_TIMEOUT: Int = 60 * 1000

    const val WRITE_TIMEOUT: Int = 60 * 1000
    const val BASE_URL= "https://sandbox.safaricom.co.ke/"
    const val BUSINESS_SHORT_CODE = "174379"
    const val PASS_KEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
    const val TRANSACTION_TYPE = "CustomerPayBillOnline"
    const val PARTY_B = "174379"
    const val CALLBACK_URL = "https://mydomain.com/path"
    // Consumer credentials
    const val CONSUMER_KEY = "okFGlndKNWRMKVkIUmWrJAITNFDBWnec7cyjnSyASY1qxVCP"
    const val CONSUMER_SECRET = "WrzhQZPtaK7VzznkOZBmKEGT3AZlNdKQIWfpP3grYHVMguvvYTGNI4x66UhZYrmv"
}
