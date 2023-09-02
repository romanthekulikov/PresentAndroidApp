package com.example.present.presentPack

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.present.data.MainInfoRepositoryImpl
import com.example.present.domain.mainUseCase.GetProgressUseCase

class PresentViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val mainInfoRepository by lazy(LazyThreadSafetyMode.NONE) {
        MainInfoRepositoryImpl(context = context)
    }

    private val getProgressUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetProgressUseCase(mainInfoRepository = mainInfoRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return PresentViewModel(getProgressUseCase) as T
    }
}