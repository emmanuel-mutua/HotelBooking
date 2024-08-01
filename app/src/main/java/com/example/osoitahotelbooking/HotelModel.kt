package com.example.osoitahotelbooking

import android.os.Parcel
import android.os.Parcelable

data class HotelModel(
    val name: String,
    val location: String,
    val price: Double,
    val checkInDate: String,
    val checkOutDate: String,
    var isBooked: Boolean=false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeDouble(price)
        parcel.writeString(checkInDate)
        parcel.writeString(checkOutDate)
        parcel.writeByte(if (isBooked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelModel> {
        override fun createFromParcel(parcel: Parcel): HotelModel {
            return HotelModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelModel?> {
            return arrayOfNulls(size)
        }
    }
}
