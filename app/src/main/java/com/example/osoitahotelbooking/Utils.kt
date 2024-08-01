package com.example.osoitahotelbooking
import android.util.Base64
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Utils {
    val timestamp: String
        get() = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

    fun phoneNumber(phone: String): String {
        if (phone == "") {
            return ""
        }

        if ((phone.length < 11) and phone.startsWith("0")) {
            val p = phone.replaceFirst("^0".toRegex(), "254")
            return p
        }
        if (phone.length == 13 && phone.startsWith("+")) {
            val p = phone.replaceFirst("^+".toRegex(), "")
            return p
        }
        return phone
    }

    fun getPassword(password : String): String {
        //encode the password to Base64
        return Base64.encodeToString(password.toByteArray(), Base64.NO_WRAP)
    }
}

