package com.muvi.viewmodel.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    application: Application,
    private val contentId: Int
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java))
            return DetailViewModel(Application(), contentId) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}