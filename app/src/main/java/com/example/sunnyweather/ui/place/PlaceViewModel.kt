package com.example.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.PlaceResponse

class PlaceViewModel:ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<PlaceResponse.Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData){
        query -> Repository.searchPlaces(query)
    }
    fun searchPlaces(query:String) {
        searchLiveData.value = query
    }
    fun savePlace(place: PlaceResponse.Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavedPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()
}