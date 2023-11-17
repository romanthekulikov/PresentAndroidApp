package com.example.present.domain.mainUseCase

import com.example.present.data.models.PresentModel
import com.example.present.data.StringProvider

class GetPresentListUseCase {
    fun execute(): List<PresentModel> {
        val presentList: MutableList<PresentModel> = mutableListOf()
        val presentNames = StringProvider.presentNameList
        val size = presentNames.size
        for (i in 0 until size) {
            presentList.add(PresentModel(presentNames[i], i + 1))
        }

        return presentList
    }
}