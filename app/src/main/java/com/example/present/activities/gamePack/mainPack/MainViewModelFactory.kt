package com.example.present.activities.gamePack.mainPack

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.present.data.MainInfoRepositoryImpl
import com.example.present.domain.mainUseCase.AddProgressUseCase
import com.example.present.domain.mainUseCase.CheckCodeUseCase
import com.example.present.domain.mainUseCase.GetProgressUseCase

class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val mainInfoRepository by lazy(LazyThreadSafetyMode.NONE) {
        MainInfoRepositoryImpl(context = context)
    }

    private val checkCodeUseCase by lazy(LazyThreadSafetyMode.NONE) {
        CheckCodeUseCase(mainInfoRepository = mainInfoRepository)
    }

    private val addProgressUseCase by lazy(LazyThreadSafetyMode.NONE) {
        AddProgressUseCase(mainInfoRepository = mainInfoRepository)
    }

    private val getProgressUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetProgressUseCase(mainInfoRepository = mainInfoRepository)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainViewModel(
            checkCodeUseCase,
            addProgressUseCase,
            getProgressUseCase
        ) as T
    }
}