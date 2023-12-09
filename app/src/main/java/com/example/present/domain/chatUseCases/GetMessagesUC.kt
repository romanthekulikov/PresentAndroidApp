package com.example.present.domain.chatUseCases

import com.example.present.data.models.Message
import com.google.firebase.database.DataSnapshot

class GetMessagesUC {
    fun execute(snapshots: DataSnapshot): MutableList<Message> {
        val messagesList = mutableListOf<Message>()
        for (snapshot in snapshots.children) {
            val message = snapshot.getValue(Message::class.java)
            if (message != null) {
                message.messageId = snapshot.key
                messagesList.add(message)
            }
        }

        return messagesList
    }
}