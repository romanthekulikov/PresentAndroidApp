package com.example.present.activities.gamePack.chatPack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.data.models.Message
import com.example.present.domain.chatUseCases.DeleteMessageUseCase
import com.example.present.domain.chatUseCases.EditMessageUseCase
import com.example.present.domain.chatUseCases.GetMessagesUC
import com.example.present.domain.chatUseCases.SendMessageUseCase
import com.example.present.domain.chatUseCases.ShowGetMessagesErrorUseCase
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.lang.Exception

const val CHAT_ROOMS_FIREBASE = "chat_rooms"
const val CHAT_MESSAGES = "messages"

class ChatViewModel(
    private val sendToFirebaseUC: SendMessageUseCase,
    private val getMessagesUC: GetMessagesUC,
    private val showGetMessagesErrorUC: ShowGetMessagesErrorUseCase,
    private val deleteMessageUC: DeleteMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    chatId: Int
) : ViewModel() {
    private val firebaseDB = Firebase.database
    private var chatReference = firebaseDB.getReference(CHAT_ROOMS_FIREBASE).child(chatId.toString())
        .child(CHAT_MESSAGES)
    var messagesList: MutableLiveData<MutableList<Message>?> = MutableLiveData(mutableListOf())
    var userId = 1
    var replayId: String? = null
    var isTextEdit = false
    var editableMessageId: String? = null

    var searching: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getMessages() {
        chatReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                messagesList.value = getMessagesUC.execute(snapshots = snapshots)
            }

            override fun onCancelled(error: DatabaseError) {
                showGetMessagesErrorUC.execute(error = error)
            }
        })
    }

    fun sendMessageToFirebase(text: String) {
        sendToFirebaseUC.execute(
            text = text,
            chatReference = chatReference,
            userId = userId,
            replayId = replayId
        )
    }

    fun editMessage(message: Message) {
        try {
            editMessageUseCase.execute(message = message, chatReference = chatReference)
        } catch (_: Exception) {}
    }

    fun deleteMessage(messageId: String) {
        try {
            deleteMessageUC.execute(messageId = messageId, chatReference = chatReference)
        } catch (_: Exception) {}

    }
}