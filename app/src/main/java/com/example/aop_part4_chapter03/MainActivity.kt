package com.example.aop_part4_chapter03

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.aop_part4_chapter03.databinding.ActivityMainBinding
import com.example.aop_part4_chapter03.model.LocationLatLngEntity
import com.example.aop_part4_chapter03.model.SearchResultEntity
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapPOIItem
import com.skt.Tmap.TMapTapi
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTmapAPI()
        initAdapter()
        initViews()
        initData()
        setData()
        bindSearchButton()
    }

    private fun initTmapAPI() {
        val tmapAPI : TMapTapi = TMapTapi(this)
        tmapAPI.setSKTMapAuthentication(getString(R.string.Tmap_Key))
    }

    private fun initAdapter() {
        adapter = SearchRecyclerViewAdapter()
    }

    private fun initViews() = with(binding){
        noResultTextView.isVisible = false
        resultRecyclerView.adapter = adapter
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    private fun setData() {
        val dataList = (0..10).map {
            SearchResultEntity(
                name = "빌딩 $it",
                fullAdress = "주소 $it",
                locationLatLng = LocationLatLngEntity(
                    latitude = it.toFloat(),
                    longtitude = it.toFloat()
                )
            )
        }
        adapter.setSearchResultListener(dataList) {
            startActivity(Intent(this, MapViewActivity::class.java))
        }
    }

    private fun bindSearchButton() {
        binding.searchButton.setOnClickListener {
            val tmapData : TMapData = TMapData()
            tmapData.findAllPOI(binding.searchEditText.text.toString(), object : TMapData.FindAllPOIListenerCallback{
                override fun onFindAllPOI(items: ArrayList<TMapPOIItem>?) {
                    items?.let {
                        it.forEachIndexed { index, tMapPOIItem ->
                            Log.i("POI_ITEM", "${tMapPOIItem.poiName}, ${tMapPOIItem.poiAddress}, ${tMapPOIItem.poiContent}")
                        }
                    }
                }
            })
        }
    }
}