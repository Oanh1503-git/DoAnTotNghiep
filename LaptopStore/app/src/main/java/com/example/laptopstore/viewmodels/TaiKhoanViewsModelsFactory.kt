package com.example.laptopstore.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TaiKhoanViewsModelsFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaiKhoanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaiKhoanViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
