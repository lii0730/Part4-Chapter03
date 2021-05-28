package com.example.aop_part4_chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aop_part4_chapter03.databinding.ActivityMapViewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.skt.Tmap.TMapView

class MapViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapViewBinding
    private var map: GoogleMap? = null
    private lateinit var mapFragment: SupportMapFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogleMap()
    }

    private fun setupGoogleMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        val seoul : LatLng = LatLng(37.56, 126.97)
        val markerOptions : MarkerOptions = MarkerOptions()
        markerOptions.position(seoul)

        this.map?.addMarker(markerOptions)
    }

    override fun onStart() {
        super.onStart()
        mapFragment.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapFragment.onStop()
    }
}