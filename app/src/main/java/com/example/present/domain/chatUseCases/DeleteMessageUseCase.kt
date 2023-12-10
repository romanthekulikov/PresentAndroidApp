package com.example.present.domain.chatUseCases

import com.google.firebase.database.DatabaseReference

class DeleteMessageUseCase {
    fun execute(messageId: String, chatReference: DatabaseReference) {
        chatReference.child(messageId).removeValue()
    }
}