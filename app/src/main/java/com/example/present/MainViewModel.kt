package com.example.present

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.data.StringProvider
import com.example.present.domain.mainUseCase.AddProgressUseCase
import com.example.present.domain.mainUseCase.CheckCodeUseCase
import com.example.present.domain.mainUseCase.GetProgressUseCase

const val BEGIN_PROGRESS = 0
const val BEGIN_HINT = "Первая подсказка"
const val BEGIN_ADDITIONAL_HINT = "Первая дополнительная подсказка"

class MainViewModel(
    private val checkCodeUseCase: CheckCodeUseCase,
    private val addProgressUseCase: AddProgressUseCase,
    private val getProgressUseCase: GetProgressUseCase
) : ViewModel() {
    var mutableProgress: MutableLiveData<Int> = MutableLiveData(BEGIN_PROGRESS)
    var mutableHint: MutableLiveData<String> = MutableLiveData(BEGIN_HINT)
    private var mutableAdditionalHint: MutableLiveData<String> =
        MutableLiveData(BEGIN_ADDITIONAL_HINT)

    fun checkCode(code: String): Boolean {
        return checkCodeUseCase.execute(code)
    }

    fun addProgress() {
        addProgressUseCase.execute(mutableProgress.value!!)
        mutableProgress.value = mutableProgress.value!! + 1
        getData()
    }

    fun getData() {
        mutableProgress.value = getProgressUseCase.execute()
        mutableHint.value = StringProvider.hintMap[mutableProgress.value]
        mutableAdditionalHint.value = StringProvider.additionalHintMap[mutableProgress.value]
    }
}