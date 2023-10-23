package com.example.present.activities.welcomePack

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.text.format.DateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.domain.welcomeUseCase.GetCountStringUseCase
import java.text.SimpleDateFormat
import java.util.Date


const val BEGIN_STRING_COUNT = "0ч:0мин:0сек"
const val BEGIN_FINISH_COUNT = false

class WelcomeViewModel(
    private val getCountStringUseCase: GetCountStringUseCase
) : ViewModel() {
    var mutableStringCount: MutableLiveData<String> = MutableLiveData(BEGIN_STRING_COUNT)
    var mutableFinishCount: MutableLiveData<Boolean> = MutableLiveData(BEGIN_FINISH_COUNT)

    fun initCount() {
        val countDownTimer =
            object : CountDownTimer(getTime(), 1000) {
                override fun onTick(millis: Long) {
                    getCountString(millis)
                }
                override fun onFinish() {
                    mutableFinishCount.value = true
                }
            }

        countDownTimer.start()
    }

    fun getCountString(requiredTime: Long) {
        mutableStringCount.value = getCountStringUseCase.execute(requiredTime)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): Long {
        val formatter = SimpleDateFormat("dd.MM.yyyy, HH:mm")
        val oldTime = DateFormat.format("dd.MM.yyyy, HH:mm", System.currentTimeMillis()).toString()
        val newTime = "21.10.2023, 11:00" //Timer date 2
        val newDate = formatter.parse(newTime)?.time
        val oldDate = formatter.parse(oldTime)?.time
        return newDate!! - oldDate!!
    }
}