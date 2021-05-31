package com.example.aop_part4_chapter03.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultEntity(
    var fullAdress : String,
    var name : String,
    var  locationLatLng : LocationLatLngEntity
) : Parcelable