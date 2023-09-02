package com.example.present.presentPack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.domain.mainUseCase.GetProgressUseCase

const val BEGIN_PROGRESS = 0

class PresentViewModel(
    private val getProgressUseCase: GetProgressUseCase
) : ViewModel() {
    var progress: MutableLiveData<Int> = MutableLiveData(BEGIN_PROGRESS)

    fun getData() {
        progress.value = getProgressUseCase.execute()
    }
}