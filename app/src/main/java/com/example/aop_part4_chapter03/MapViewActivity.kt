package com.example.aop_part4_chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aop_part4_chapter03.databinding.ActivityMapViewBinding
import com.example.aop_part4_chapter03.model.SearchResultEntity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
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

        getData()?.let {
            val location : LatLng = LatLng(it.locationLatLng.latitude.toDouble(), it.locationLatLng.longtitude.toDouble())
            val cameraPosition = CameraPosition.builder()
                .target(location)
                .zoom(17f)
                .build()
            this.map?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            val markerOptions : MarkerOptions = MarkerOptions()
            markerOptions.position(location)
            markerOptions.title(it.name)
            markerOptions.snippet(it.fullAdress)

            this.map?.addMarker(markerOptions)?.showInfoWindow()
        }
    }

    private fun getData() : SearchResultEntity?{
        //TODO: Intent로 Data 넘어오는 것 까지 확인 했고
        var item : SearchResultEntity? = null
        if(intent.getParcelableExtra<SearchResultEntity>("POI_ITEM") == null) {
           return null
        } else {
            item = intent.getParcelableExtra<SearchResultEntity>("POI_ITEM") as SearchResultEntity
            return item
        }
    }
}