package com.example.aop_part4_chapter03

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aop_part4_chapter03.databinding.ActivityMainBinding
import com.example.aop_part4_chapter03.model.LocationLatLngEntity
import com.example.aop_part4_chapter03.model.SearchResultEntity
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapPOIItem
import com.skt.Tmap.TMapTapi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job : Job
    private lateinit var binding: ActivityMainBinding
    private lateinit var POIAdapter : TmapPOIAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initTmapAPI()
        initAdapter()
        initViews()
        initData()
        bindSearchButton()
    }

    private fun initTmapAPI() {
        val tmapAPI = TMapTapi(this)
        tmapAPI.setSKTMapAuthentication(getString(R.string.Tmap_Key))
    }

    private fun initAdapter() {
        POIAdapter = TmapPOIAdapter(onItemClicked = {
            val intent = Intent(this, MapViewActivity::class.java)
            val resultEntity : SearchResultEntity = createParcelableItem(it)
            intent.putExtra("POI_ITEM", resultEntity)
            startActivity(intent)
        })
    }

    private fun initViews() = with(binding){
        noResultTextView.isVisible = false
        resultRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        resultRecyclerView.adapter = POIAdapter
    }

    private fun initData() {
        POIAdapter.notifyDataSetChanged()
    }

    private fun bindSearchButton() = with(binding) {
        searchButton.setOnClickListener {
            search(searchEditText.text.toString())
        }
    }

    private fun search(searchKeyword : String) {
        val tmapData = TMapData()
        try{
            launch {
                withContext(Dispatchers.IO){
                    val poiITems = tmapData.findAllPOI(searchKeyword)

                    withContext(Dispatchers.Main) {
                        POIAdapter.submitList(poiITems)
                    }
                }
            }
        } catch (e: Exception){
            Log.i("bindSearchButton_error", e.toString())
        }
    }

    private fun createParcelableItem(item : TMapPOIItem) : SearchResultEntity{
        val fullAdress = "${item.upperAddrName} ${item.middleAddrName} ${item.lowerAddrName}"
        val result = SearchResultEntity(fullAdress, item.poiName, LocationLatLngEntity(item.noorLat.toFloat(), item.noorLon.toFloat()))
        return result
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}


