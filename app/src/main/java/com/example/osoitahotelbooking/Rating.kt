package com.example.osoitahotelbooking
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Rating : Fragment() {
    private lateinit var ratingBar: RatingBar
    private lateinit var submitButton: Button
    private lateinit var ratingsRef: DatabaseReference
    private lateinit var averageRatingTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratingBar = view.findViewById(R.id.ratingBar)
        submitButton = view.findViewById(R.id.rate_button)
        averageRatingTextView = view.findViewById(R.id.average_rating)

        val database = FirebaseDatabase.getInstance()
        ratingsRef = database.reference.child("ratings")

        setupRatingSubmission()
        calculateAverageRating()
    }

    private fun setupRatingSubmission() {
        submitButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                Log.d("Rating", "User is not authenticated, redirecting to login.")
                redirectToLogin()
            } else {
                val rating = ratingBar.rating
                submitRatingToFirebase(rating)
            }
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun submitRatingToFirebase(rating: Float) {
        val ratingData = RatingData(rating)
        ratingsRef.push().setValue(ratingData)
            .addOnSuccessListener {
                showToast("Rating submitted successfully!")
            }
            .addOnFailureListener {
                Log.e("Rating", "Error submitting rating", it)
                showToast("Failed to submit rating. Please try again.")
            }
    }

    private fun calculateAverageRating() {
        ratingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalRating = 0f
                var ratingCount = 0
                for (ratingSnapshot in dataSnapshot.children) {
                    val ratingData = ratingSnapshot.getValue(RatingData::class.java)
                    if (ratingData != null) {
                        totalRating += ratingData.rating
                        ratingCount++
                    }
                }
                if (ratingCount > 0) {
                    val averageRating = totalRating / ratingCount
                    Log.d("Rating", "Average rating calculated: $averageRating")
                    updateHotelRating(averageRating)
                } else {
                    Log.d("Rating", "No ratings available to calculate average.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("RatingFragment", "loadRating:onCancelled", databaseError.toException())
                showToast("Error loading ratings. Please try again.")
            }
        })
    }

    private fun updateHotelRating(averageRating: Float) {
        activity?.runOnUiThread {
            Log.d("Rating", "Updating TextView with average rating: $averageRating")
            averageRatingTextView.text = String.format("Average Rating: %.1f", averageRating)

            when {
                averageRating >= 4.5 -> {
                    showToast("Hotel rating: 5 star")
                }
                averageRating >= 3.5 -> {
                    showToast("Hotel rating: 4 star")
                }
                averageRating >= 2.5 -> {
                    showToast("Hotel rating: 3 star")
                }
                averageRating >= 1.5 -> {
                    showToast("Hotel rating: 2 star")
                }
                else -> {
                    showToast("Hotel rating: 1 star")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    data class RatingData(val rating: Float = 0f)
}
