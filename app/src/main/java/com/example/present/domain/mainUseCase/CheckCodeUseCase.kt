package com.example.present.domain.mainUseCase

import com.example.present.domain.MainInfoRepository


class CheckCodeUseCase(private val mainInfoRepository: MainInfoRepository) {
    private val progressToCodeMap: Map<Int, String> = mapOf(
        0 to "8528",
        1 to "3847",
        2 to "1695"
    )

    fun execute(code: String): Boolean {
        val progress = mainInfoRepository.getProgress()
        val requiredCode = progressToCodeMap[progress]

        return code == requiredCode
    }

}