package com.example.present.activities.gamePack.chatPack

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.present.domain.chatUseCases.GetMessagesUC
import com.example.present.domain.chatUseCases.SendMessageToFirebaseUseCase
import com.example.present.domain.chatUseCases.ShowGetMessagesErrorUseCase

class ChatViewModelFactory(activity: Activity) : ViewModelProvider.Factory {

    private val sendToFirebaseUC by lazy(LazyThreadSafetyMode.NONE) {
        SendMessageToFirebaseUseCase()
    }

    private val getMessagesUC by lazy(LazyThreadSafetyMode.NONE) {
        GetMessagesUC()
    }

    private val showGetMessagesErrorUC by lazy(LazyThreadSafetyMode.NONE) {
        ShowGetMessagesErrorUseCase(activity = activity)
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ChatViewModel(
            sendToFirebaseUC,
            getMessagesUC,
            showGetMessagesErrorUC
        ) as T
    }
}