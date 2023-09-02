package com.example.present.welcomePack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.present.domain.welcomeUseCase.GetCountStringUseCase

class WelcomeViewModelFactory : ViewModelProvider.Factory {
    private val getCountStringUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetCountStringUseCase()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return WelcomeViewModel(getCountStringUseCase) as T
    }
}