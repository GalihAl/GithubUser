package com.galih.latihandicoding.submission1.githubuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.galih.latihandicoding.submission1.githubuser.viewModel.ThemeViewModel

class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}