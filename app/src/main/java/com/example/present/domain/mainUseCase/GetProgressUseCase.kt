package com.example.present.domain.mainUseCase

import com.example.present.domain.MainInfoRepository

class GetProgressUseCase(private val mainInfoRepository: MainInfoRepository) {
    fun execute(): Int {
        return mainInfoRepository.getProgress()
    }
}