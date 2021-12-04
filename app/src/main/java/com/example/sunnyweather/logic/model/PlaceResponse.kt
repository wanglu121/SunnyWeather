package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status:String,val places:List<Place>)
data class Place(val Id:String,val name:String, @SerializedName("formatted_address")val address:String,
                 val location:Location,val place_id:String)
data class Location(val lng:String,val lat:String)