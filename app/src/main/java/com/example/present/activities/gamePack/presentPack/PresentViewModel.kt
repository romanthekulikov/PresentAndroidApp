package com.example.present.activities.gamePack.presentPack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.data.models.PresentModel
import com.example.present.domain.mainUseCases.GetPresentListUseCase
import com.example.present.domain.mainUseCases.GetProgressUseCase

const val BEGIN_PROGRESS = 0

class PresentViewModel(
    private val getProgressUseCase: GetProgressUseCase,
    private val getPresentListUseCase: GetPresentListUseCase
) : ViewModel() {
    var progress: MutableLiveData<Int> = MutableLiveData(BEGIN_PROGRESS)

    fun getData() {
        progress.value = getProgressUseCase.execute()
    }

    fun getPresentList(): List<PresentModel> {
        return getPresentListUseCase.execute()
    }
}