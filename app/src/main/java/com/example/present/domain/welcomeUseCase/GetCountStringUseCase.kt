package com.example.present.domain.welcomeUseCase

class GetCountStringUseCase {
    fun execute(time: Long): String {
        val d = (time / (1000*60*60*24)).toInt()
        val h = ((time - d*1000*60*60*24)/(1000*60*60)).toInt()
        val min = ((time - h*1000*60*60 - d*1000*60*60*24)/(1000*60)).toInt()
        val sec = ((time - min*1000*60 - h*1000*60*60 - d*1000*60*60*24)/1000).toInt()
        return "$d дней : $h час\n$min мин : $sec сек"
    }
}