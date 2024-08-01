package com.example.osoitahotelbooking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HotelAdapter(
    private val hotels: List<HotelModel>,
    private val context: Context,
    private val navigateToBookingFragment: (HotelModel) -> Unit
) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    inner class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomImageView: ImageView = itemView.findViewById(R.id.roomImageView)
        val roomNameTextView: TextView = itemView.findViewById(R.id.roomNameTextView)
        val roomLocationTextView: TextView = itemView.findViewById(R.id.roomLocationTextView)
        val roomPriceTextView: TextView = itemView.findViewById(R.id.roomPriceTextView)
        private val bookButton: Button = itemView.findViewById(R.id.book_button)

        init {
            bookButton.setOnClickListener {
                val hotel = hotels[adapterPosition]
                navigateToBookingFragment(hotel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hotel_item, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotels[position]
        holder.roomImageView.setImageResource(getImageResourceId(position))
        holder.roomNameTextView.text = hotel.name
        holder.roomLocationTextView.text = hotel.location
        holder.roomPriceTextView.text = "Price: ksh${hotel.price}"
    }

    override fun getItemCount(): Int = hotels.size

    private fun getImageResourceId(position: Int): Int {
        return when (position) {
            0 -> R.drawable.room1
            1 -> R.drawable.room2
            2 -> R.drawable.room3
            3 -> R.drawable.room4
            4 -> R.drawable.room5
            5 -> R.drawable.room6
            6 -> R.drawable.room7
            7 -> R.drawable.room8
            8 -> R.drawable.room9
            9 -> R.drawable.room10
            else -> R.drawable.default_room
        }
    }
}
