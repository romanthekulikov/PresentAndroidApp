package com.example.present.domain.welcomeUseCase

import android.text.format.DateFormat
import com.example.present.data.StringProvider

class GetCountStringUseCase {
    fun execute(time: Long): String {
        return DateFormat.format(StringProvider.COUNTER_SCHEMA, time).toString()
    }
}