package com.example.aop_part4_chapter03.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationLatLngEntity(
    var latitude : Double,
    var longtitude : Double
) : Parcelable
