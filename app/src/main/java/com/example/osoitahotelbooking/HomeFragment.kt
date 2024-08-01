package com.example.osoitahotelbooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var hotelAdapter: HotelAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Corrected line with requireContext()

        val hotels = listOf(
            HotelModel("Room A", "Nairobi", 1000.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room B", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room C", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room D", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room E", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room F", "Nairobi", 4000.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room G", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room H", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room I", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false),
            HotelModel("Room J", "Nairobi", 1150.00, "2024-06-12", "2024-06-15", false)
        )

        hotelAdapter = HotelAdapter(hotels, requireContext()) { hotel: HotelModel ->
            val action = HomeFragmentDirections.actionHomeFragmentToBookingFragment(hotel)
            findNavController().navigate(action)
        }

        recyclerView.adapter = hotelAdapter

        return view
    }
}
