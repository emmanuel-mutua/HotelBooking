package com.example.osoitahotelbooking

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.osoitahotelbooking.Utils.getPassword
import com.example.osoitahotelbooking.Utils.timestamp
import com.example.osoitahotelbooking.payment.AccessToken
import com.example.osoitahotelbooking.payment.STKPush
import com.example.osoitahotelbooking.services.DarajaApiClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.MessageDigest
import java.util.Calendar

class BookingFragment : Fragment() {

    private val args: BookingFragmentArgs by navArgs()
    private lateinit var hotel: HotelModel
    private lateinit var darajaApiClient: DarajaApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hotel = args.hotel
        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        val roomNumberEditText: EditText = view.findViewById(R.id.roomNumber)
        val phoneNumber: EditText = view.findViewById(R.id.phoneNumber)
        val checkInDateEditText: TextView = view.findViewById(R.id.check_in_date)
        val checkOutDateEditText: TextView = view.findViewById(R.id.check_out_date)
        val amountValueEditText: EditText = view.findViewById(R.id.amount_value)
        val confirmButton: Button = view.findViewById(R.id.confirm_button)
        val cancelButton: Button = view.findViewById(R.id.cancel_button)
        val bookingStatusTextView: TextView = view.findViewById(R.id.booking_status)

        checkInDateEditText.text = hotel.checkInDate
        checkOutDateEditText.text = hotel.checkOutDate
        amountValueEditText.setText(getString(R.string.amount, hotel.price))
        fetchBookingStatus(bookingStatusTextView)

        darajaApiClient = DarajaApiClient().apply {
            setIsDebug(true)
        }
        fetchAccessToken()

        checkInDateEditText.setOnClickListener {
            showDatePickerDialog(checkInDateEditText)
        }

        checkOutDateEditText.setOnClickListener {
            showDatePickerDialog(checkOutDateEditText)
        }

        confirmButton.setOnClickListener {
            if (validateInputs(roomNumberEditText, checkInDateEditText, checkOutDateEditText, amountValueEditText)) {
                if (!hotel.isBooked) {
                    // Initiate M-Pesa payment
                    val amount = hotel.price.toInt().toString()
                    initiateMpesaPayment(amount, phoneNumber.text.toString().trim())
                } else {
                    Toast.makeText(requireContext(), R.string.room_already_booked, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), R.string.fill_in_all_fields, Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            cancelBooking(bookingStatusTextView)
        }

        return view
    }

    private fun showDatePickerDialog(dateTextView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "${selectedDayOfMonth}/${selectedMonth + 1}/${selectedYear}"
                dateTextView.text = selectedDate
            }, year, month, dayOfMonth)

        datePickerDialog.show()
    }

    private fun validateInputs(
        roomNumberEditText: EditText,
        checkInDateEditText: TextView,
        checkOutDateEditText: TextView,
        amountValueEditText: EditText
    ): Boolean {
        val roomNumber = roomNumberEditText.text.toString().trim()
        val checkInDate = checkInDateEditText.text.toString().trim()
        val checkOutDate = checkOutDateEditText.text.toString().trim()
        val amount = amountValueEditText.text.toString().trim()

        return roomNumber.isNotEmpty() && checkInDate.isNotEmpty() && checkOutDate.isNotEmpty() && amount.isNotEmpty()
    }

    private fun fetchBookingStatus(bookingStatusTextView: TextView) {
        val database = FirebaseDatabase.getInstance()
        val hotelRef = database.getReference("hotels").child(hotel.name)

        hotelRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isBooked = snapshot.child("isBooked").getValue(Boolean::class.java) ?: false
                hotel.isBooked = isBooked
                updateBookingStatus(bookingStatusTextView)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), R.string.fetch_booking_status_failed, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateBookingStatus(bookingStatusTextView: TextView) {
        bookingStatusTextView.text = if (hotel.isBooked) {
            getString(R.string.booking_status_booked)
        } else {
            getString(R.string.booking_status_available)
        }
    }

    private fun cancelBooking(bookingStatusTextView: TextView) {
        val database = FirebaseDatabase.getInstance()
        val hotelRef = database.getReference("hotels").child(hotel.name)

        hotelRef.child("isBooked").setValue(false)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    hotel.isBooked = false
                    updateBookingStatus(bookingStatusTextView)
                    Toast.makeText(requireContext(), R.string.booking_cancelled, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), R.string.cancellation_failed, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchAccessToken() {
        darajaApiClient.setGetAccessToken(true)
        darajaApiClient.mpesaService().getAccessToken().enqueue(object : retrofit2.Callback<AccessToken> {
            override fun onResponse(call: retrofit2.Call<AccessToken>, response: retrofit2.Response<AccessToken>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        darajaApiClient.setAuthToken(it.accessToken)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<AccessToken>, t: Throwable) {
                // Handle failure
            }
        })
    }


    private fun initiateMpesaPayment(amount: String, phoneNumber: String) {
        println("starting payment")
        val passwordToEncode = "${Constants.BUSINESS_SHORT_CODE}${Constants.PASS_KEY}${timestamp}"
        val encodedPassword = getPassword(passwordToEncode)
        val stkPush = STKPush(
            businessShortCode = Constants.BUSINESS_SHORT_CODE,
            password = encodedPassword,
            timestamp = timestamp,
            transactionType = Constants.TRANSACTION_TYPE,
            amount = amount,
            partyA = Utils.phoneNumber("0714024974"),
            partyB = Constants.PARTY_B,
            phoneNumber = Utils.phoneNumber(phoneNumber),
            callBackURL = Constants.CALLBACK_URL,
            accountReference = getString(R.string.room_number_placeholder),
            transactionDesc = getString(R.string.booking)
        )
        darajaApiClient.setGetAccessToken(false)
        darajaApiClient.mpesaService().sendPush(stkPush).enqueue(object : retrofit2.Callback<STKPush> {
            override fun onResponse(call: retrofit2.Call<STKPush>, response: retrofit2.Response<STKPush>) {
                try {
                    if (response.isSuccessful) {
                        Log.d("MPESA", "onResponse: " + response.body())
                        submitBookingToFirebase(hotel, requireView().findViewById(R.id.booking_status))
                    } else {
                        Log.d("MPESA", "onResponse: " + response.errorBody())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: retrofit2.Call<STKPush>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    @SuppressLint("MissingPermission")
    private fun submitBookingToFirebase(hotel: HotelModel, bookingStatusTextView: TextView) {
        val database = FirebaseDatabase.getInstance()
        val hotelRef = database.getReference("hotels").child(hotel.name)

        hotelRef.child("isBooked").setValue(true)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    hotel.isBooked = true
                    updateBookingStatus(bookingStatusTextView)
                    Toast.makeText(requireContext(), R.string.booking_successful, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), R.string.cancellation_failed, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
