package com.example.present.activities.gamePack.chatPack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.present.data.models.Message
import com.example.present.domain.chatUseCases.GetMessagesUC
import com.example.present.domain.chatUseCases.SendMessageToFirebaseUseCase
import com.example.present.domain.chatUseCases.ShowGetMessagesErrorUseCase
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

const val CHAT_ROOMS_FIREBASE = "chat_rooms"
const val CHAT_MESSAGES = "messages"

class ChatViewModel(
    private val sendToFirebaseUC: SendMessageToFirebaseUseCase,
    private val getMessagesUC: GetMessagesUC,
    private val showGetMessagesErrorUC: ShowGetMessagesErrorUseCase
) : ViewModel() {
    private val firebaseDB = Firebase.database
    private var chatReference = firebaseDB.getReference(CHAT_ROOMS_FIREBASE).child("1")
        .child(CHAT_MESSAGES)
    var messagesList: MutableLiveData<MutableList<Message>?> = MutableLiveData(mutableListOf())
    var userId = 1
    var replayId: String? = null

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
}