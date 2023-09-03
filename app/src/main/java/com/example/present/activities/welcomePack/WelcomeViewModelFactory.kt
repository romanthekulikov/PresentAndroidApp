package com.example.present.activities.welcomePack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.present.domain.welcomeUseCase.GetCountStringUseCase

class WelcomeViewModelFactory : ViewModelProvider.Factory {
    private val getCountStringUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetCountStringUseCase()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return WelcomeViewModel(getCountStringUseCase) as T
    }
}