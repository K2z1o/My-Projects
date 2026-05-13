package com.example.ereport.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.ereport.api.ApiService
import com.example.ereport.model.Ostrzezenie
import kotlinx.coroutines.launch

class OstrzezeniaViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _items = MutableLiveData<List<Ostrzezenie>>()
    val items: LiveData<List<Ostrzezenie>> = _items

    private var currentSort = "Tytul"
    private var currentMiejscowoscId = ""

    fun load(miejscowoscId: String, sortBy: String = currentSort) {
        currentMiejscowoscId = miejscowoscId
        currentSort = sortBy
        apiService.getOstrzezenia(miejscowoscId, sortBy).enqueue(object : retrofit2.Callback<List<Ostrzezenie>> {
            override fun onResponse(call: retrofit2.Call<List<Ostrzezenie>>, response: retrofit2.Response<List<Ostrzezenie>>) {
                _items.postValue(response.body() ?: emptyList())
            }
            override fun onFailure(call: retrofit2.Call<List<Ostrzezenie>>, t: Throwable) {
                _items.postValue(emptyList())
            }
        })
    }

    fun sortByTytul() = load(currentMiejscowoscId, "Tytul")
    fun sortByCzas() = load(currentMiejscowoscId, "CzasRozpoczecia")

    fun delete(id: String, miejscowoscId: String) {
        apiService.deleteOstrzezenie(id).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                load(miejscowoscId)
            }
            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {}
        })
    }
}