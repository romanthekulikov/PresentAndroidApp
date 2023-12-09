package com.example.present.domain.mainUseCases

import com.example.present.domain.MainInfoRepository

class AddProgressUseCase(private val mainInfoRepository: MainInfoRepository) {

    fun execute(currentProgress: Int) {
        mainInfoRepository.addProgress(currentProgress + 1)
    }
}