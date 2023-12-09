package com.example.present.domain.mainUseCases

import com.example.present.domain.MainInfoRepository

class GetProgressUseCase(private val mainInfoRepository: MainInfoRepository) {
    fun execute(): Int {
        return mainInfoRepository.getProgress()
    }
}