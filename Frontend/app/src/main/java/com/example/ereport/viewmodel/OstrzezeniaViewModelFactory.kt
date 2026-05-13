package com.example.ereport.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ereport.api.ApiService

class OstrzezeniaViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OstrzezeniaViewModel(apiService) as T
    }
}