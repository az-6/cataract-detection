package com.dicoding.capstone.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.dicoding.capstone.R

class HospitalFragment : Fragment(R.layout.fragment_hospital) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val callback = OnMapReadyCallback { googleMap ->
        // Cek apakah aplikasi memiliki izin lokasi
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Jika izin diberikan, ambil lokasi terakhir
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener(requireActivity(), OnSuccessListener { location ->
                        if (location != null) {
                            // Jika lokasi ditemukan, tampilkan di peta
                            val userLocation = LatLng(location.latitude, location.longitude)
                            googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                        } else {
                            // Jika lokasi tidak ditemukan, beri peringatan
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 2f)) // Menampilkan koordinat default
                            Toast.makeText(requireContext(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    })
            } catch (e: SecurityException) {
                // Tangani jika terjadi masalah dengan izin lokasi
                e.printStackTrace()
                Toast.makeText(requireContext(), "Kesalahan pengambilan lokasi", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Jika izin belum diberikan, minta izin
            requestLocationPermission()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hospital, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Ambil fragment peta dan setup callback
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun requestLocationPermission() {
        // Meminta izin lokasi
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    // Menangani hasil permintaan izin
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin diberikan, coba dapatkan lokasi dan tampilkan di peta
                (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(callback)
            } else {
                // Jika izin ditolak, beri informasi kepada pengguna
                Toast.makeText(requireContext(), "Izin lokasi tidak diberikan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }
}
