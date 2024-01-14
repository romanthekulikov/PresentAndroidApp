package com.example.present.activities.gamePack.mainPack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.data.database.AppDatabase
import com.example.present.domain.mainUseCases.AddProgressUseCase
import com.example.present.domain.mainUseCases.CheckCodeUseCase
import com.example.present.domain.mainUseCases.GetDatabase
import com.example.present.domain.mainUseCases.GetProgressUseCase
import com.example.present.remote.ApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val BEGIN_PROGRESS = 0
const val BEGIN_HINT = "Первая подсказка"
const val BEGIN_ADDITIONAL_HINT = "Первая дополнительная подсказка"

class MainViewModel(
    private val checkCodeUseCase: CheckCodeUseCase,
    private val addProgressUseCase: AddProgressUseCase,
    private val getProgressUseCase: GetProgressUseCase,
    private val getDatabase: GetDatabase
) : ViewModel() {
    var mutableStageId: MutableLiveData<Int> = MutableLiveData(BEGIN_PROGRESS)
    var mutableLat: MutableLiveData<Double> = MutableLiveData(0.0)
    var mutableLong: MutableLiveData<Double> = MutableLiveData(0.0)
    var mutablePresentId: MutableLiveData<Int> = MutableLiveData(BEGIN_PROGRESS)
    var mutableNavPosition: MutableLiveData<Int> = MutableLiveData(HOME_POSITION)
    var mutableHint: MutableLiveData<String> = MutableLiveData(BEGIN_HINT)
    var mutableAdditionalHint: MutableLiveData<String> = MutableLiveData(BEGIN_ADDITIONAL_HINT)
    var taskArg: String? = ""

    fun addProgress() {
        val stageDao = getDatabase.execute().getStageDao()
        CoroutineScope(Dispatchers.IO).launch {
            stageDao.doneStage(mutableStageId.value!!)
            CoroutineScope(Dispatchers.Main).launch {
                getData()
            }
        }
    }

    fun getData() {
        val stageDao = getDatabase.execute().getStageDao()
        CoroutineScope(Dispatchers.IO).launch {
            val currentStage = stageDao.getCurrentStage()
            CoroutineScope(Dispatchers.Main).launch {
                if (currentStage != null) {
                    mutableStageId.value = currentStage.id
                    mutableHint.value = currentStage.textStage
                    mutableAdditionalHint.value = currentStage.textHint
                    mutablePresentId.value = currentStage.idPresent
                    mutableLat.value = currentStage.latitude
                    mutableLong.value = currentStage.longitude
                } else {
                    mutableStageId.value = -1
                    mutableHint.value =  ""
                }
            }
        }

    }
}