package com.example.present.domain.chatUseCases

import com.example.present.data.models.Message
import com.google.firebase.database.DatabaseReference

class EditMessageUseCase {
    fun execute(message: Message, chatReference: DatabaseReference) {
        chatReference.child(message.messageId!!).setValue(message)
    }
}