package com.example.present.activities.gamePack.chatPack

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.present.domain.chatUseCases.DeleteMessageUseCase
import com.example.present.domain.chatUseCases.EditMessageUseCase
import com.example.present.domain.chatUseCases.GetMessagesUC
import com.example.present.domain.chatUseCases.SendMessageUseCase
import com.example.present.domain.chatUseCases.ShowGetMessagesErrorUseCase

class ChatViewModelFactory(activity: Activity) : ViewModelProvider.Factory {

    private val sendToFirebaseUC by lazy(LazyThreadSafetyMode.NONE) {
        SendMessageUseCase()
    }

    private val getMessagesUC by lazy(LazyThreadSafetyMode.NONE) {
        GetMessagesUC()
    }

    private val showGetMessagesErrorUC by lazy(LazyThreadSafetyMode.NONE) {
        ShowGetMessagesErrorUseCase(activity = activity)
    }

    private val deleteMessageUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DeleteMessageUseCase()
    }

    private val editMessageUseCase by lazy(LazyThreadSafetyMode.NONE) {
        EditMessageUseCase()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ChatViewModel(
            sendToFirebaseUC,
            getMessagesUC,
            showGetMessagesErrorUC,
            deleteMessageUseCase,
            editMessageUseCase
        ) as T
    }
}