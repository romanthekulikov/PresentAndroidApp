package com.example.present.activities.welcomePack

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.domain.welcomeUseCase.GetCountStringUseCase
import java.util.Date

const val BEGIN_STRING_COUNT = "0ч:0мин:0сек"
const val BEGIN_FINISH_COUNT = false

class WelcomeViewModel(
    private val getCountStringUseCase: GetCountStringUseCase
) : ViewModel() {
    var mutableStringCount: MutableLiveData<String> = MutableLiveData(BEGIN_STRING_COUNT)
    var mutableFinishCount: MutableLiveData<Boolean> = MutableLiveData(BEGIN_FINISH_COUNT)

    fun getCountString(requiredTime: Long) {
        mutableStringCount.value = getCountStringUseCase.execute(requiredTime)
    }

    fun initCount() {
        val countDownTimer =
            object : CountDownTimer(getTime(), 1000) {
                override fun onTick(millis: Long) {
                    getCountString(getTime())
                }

                override fun onFinish() {
                    mutableFinishCount.value = true
                }
            }

        countDownTimer.start()
    }

    @Suppress("DEPRECATION")
    fun getTime(): Long {
        return Date(123, 7, 30, 6, 21, 0).time - System.currentTimeMillis() - 10800000
    }
}