package com.example.osoitahotelbooking

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private val osoitalodge = LatLng(-1.3923088, 36.7799016)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setupMap()
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        googleMap.addMarker(MarkerOptions().position(osoitalodge).title("Marker in Osoitalodge"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(osoitalodge, 30f))
        setupMap()
        Log.d("MapsFragment", "Map is ready and marker added")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        Log.d("MapsFragment", "Requesting map async")

        // Request location permissions if not granted
        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            Log.d("MapsFragment", "Requesting location permission")
        } else {
            setupMap()
            Log.d("MapsFragment", "Location permission already granted, setting up map")
        }
    }

    private fun setupMap() {
        if (::googleMap.isInitialized) {
            enableMyLocation()
            Log.d("MapsFragment", "Map is initialized, enabling location")
        } else {
            Log.d("MapsFragment", "GoogleMap is not initialized")
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            Log.d("MapsFragment", "MyLocation is enabled on the map")
        } else {
            Toast.makeText(requireContext(), "Location permissions are required for map functionality", Toast.LENGTH_SHORT).show()
            Log.d("MapsFragment", "Location permissions are not granted")
        }
    }
}
