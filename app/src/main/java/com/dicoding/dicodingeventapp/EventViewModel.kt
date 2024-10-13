package com.dicoding.dicodingeventapp

import android.content.ContentValues.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class EventViewModel : ViewModel() {

    private val _eventsLiveData = MutableLiveData<List<Event>>()
    val eventsLiveData: LiveData<List<Event>> get() = _eventsLiveData

    private val _eventDetailLiveData = MutableLiveData<DetailResponse>()
    val eventDetailLiveData: LiveData<DetailResponse> get() = _eventDetailLiveData

    fun fetchEvents(active: Int) {
        RetrofitClient.apiService.getEvents(active).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        _eventsLiveData.value = eventResponse.listEvents
                        Log.d("EventViewModel", "Data fetched EventViewModel: ${eventResponse.listEvents}")
                    } ?: run {
                        Log.e("EventViewModel", "Response body is null")
                    }
                } else {
                    Log.e("EventViewModel", "API response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e("EventViewModel", "API call failed: ${t.message}")
            }
        })
    }

    fun fetchEventsLimit(active: Int, limit: Int) {
        RetrofitClient.apiService.getEventLimit(active, limit).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        _eventsLiveData.value = eventResponse.listEvents
                        Log.d("EventViewModel", "Data fetched EventViewModel: ${eventResponse.listEvents}")
                    } ?: run {
                        Log.e("EventViewModel", "Response body is null")
                    }
                } else {
                    Log.e("EventViewModel", "API response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e("EventViewModel", "API call failed: ${t.message}")
            }
        })
    }



    fun searchEvents(keyword: String) {
        RetrofitClient.apiService.searchEvents(-1, keyword).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        // Ambil listEvents dari eventResponse dan set ke LiveData
                        _eventsLiveData.value = eventResponse.listEvents
                        Log.d(TAG, "onResponse: $eventResponse")
                    } ?: run {
                        // Handle case if response body is null
                        Log.e("searchEvents", "Response body is null")
                    }
                } else {
                    Log.e("searchEvents", "API response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                // Handle failure case
                Log.e("searchEvents", "API call failed: ${t.message}")
            }
        })
    }

    fun fetchEventDetail(id: String) {
        RetrofitClient.apiService.getEventDetail(id).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    Log.d("EventDetailViewModel", "Raw Response: ${response.body().toString()}")
                    response.body()?.let { detailResponse ->
                        _eventDetailLiveData.value = detailResponse
                        Log.d("EventDetailViewModel", "Data fetched successfully: $detailResponse")
                    } ?: run {
                        Log.e("EventDetailViewModel", "Response body is null")
                    }
                } else {
                    Log.e("EventDetailViewModel", "API response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("EventDetailViewModel", "API call failed: ${t.message}")
            }
        })
    }
}
