package com.example.aop_part4_chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aop_part4_chapter03.databinding.ActivityMapViewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.skt.Tmap.TMapView

class MapViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMapViewBinding
    private  var map : GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapView : TMapView = TMapView(this)
        mapView.setSKTMapApiKey(getString(R.string.Tmap_Key))

//        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
//        mapFragment.getMapAsync(object: OnMapReadyCallback{
//            override fun onMapReady(googleMap: GoogleMap?) {
//                map = googleMap
//            }
//        })

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.mapView, mapFragment)
            .commit()
    }
}