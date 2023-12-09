package com.example.present.domain.chatUseCases

import com.example.present.data.models.Message
import com.google.firebase.database.DatabaseReference

class SendMessageToFirebaseUseCase {
    fun execute(text: String, chatReference: DatabaseReference, userId: Int, replayId: String?) {
        val msg = Message(
            text = text,
            time = System.currentTimeMillis(),
            userId = userId,
            replayId = replayId
        )
        chatReference.child(chatReference.push().key ?: "null").setValue(msg)
    }
}
