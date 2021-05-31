package com.example.aop_part4_chapter03

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.aop_part4_chapter03.databinding.ActivityMapViewBinding
import com.example.aop_part4_chapter03.model.LocationLatLngEntity
import com.example.aop_part4_chapter03.model.SearchResultEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.skt.Tmap.TMapData

class MapViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapViewBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener
    private var currentAddress: String = ""
    private var map: GoogleMap? = null

    inner class myLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            updateCurrentLocation(location)
            mLocationManager.removeUpdates(mLocationListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLocationManager()
        setupGoogleMap()
        bindViews()
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        getData()?.let { entity ->
            updateLocation(entity)
        }
    }

    private fun updateLocation(entity: SearchResultEntity) {
        val location = LatLng(
            entity.locationLatLng.latitude,
            entity.locationLatLng.longtitude
        )

        val cameraPosition = CameraPosition.builder()
            .target(location)
            .zoom(ZOOM_LEVEL)
            .build()
        this.map?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        val markerOptions = MarkerOptions()
        markerOptions.position(location)
        markerOptions.title(entity.name)
        markerOptions.snippet(entity.fullAdress)

        this.map?.addMarker(markerOptions)?.showInfoWindow()
    }

    private fun initLocationManager() {
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        mLocationListener = myLocationListener()
    }

    private fun setupGoogleMap() {
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun bindViews() {
        binding.currentLocationButton.setOnClickListener {
            checkPermission()
        }
    }

    private fun getData(): SearchResultEntity? {
        //TODO: Intent로 Data 넘어오는 것 까지 확인 했고
        var item: SearchResultEntity?
        if (intent.getParcelableExtra<SearchResultEntity>("POI_ITEM") == null) {
            return null
        } else {
            item = intent.getParcelableExtra<SearchResultEntity>("POI_ITEM") as SearchResultEntity
            return item
        }
    }

    private fun updateCurrentLocation(location: Location) {
        val entity = SearchResultEntity(getCurrentAddress(location), "내 위치", LocationLatLngEntity(location.latitude, location.longitude))
        updateLocation(entity)
    }

    private fun getCurrentAddress(location: Location) : String {
        try {
            val t = Thread {
                val tmapData = TMapData()
                //TODO: tmapData를 통해 ReverseGeoCoding, 얻은 위도 / 경도를 바탕으로 주소 획득
                currentAddress = tmapData.convertGpsToAddress(location.latitude, location.longitude)
            }
            t.start()
            t.join()
        } catch (e: Exception) {
            Log.e("getCurrentAddress", e.toString())
        }
        return currentAddress
    }

    private fun checkPermission() {

        val isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGpsEnabled) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //todo: 권한 허용
                setLocationListener()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocationListener() {
        //todo: Listener 설정
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            mLocationListener
        )

        mLocationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            mLocationListener
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setLocationListener()
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 100
        const val ZOOM_LEVEL = 17f
    }
}