package com.example.present.activities.gamePack.chatPack

import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.present.adapters.ChatAdapter
import com.example.present.data.models.Message
import com.example.present.databinding.ActivityChatBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

const val CHAT_ROOMS_FIREBASE = "chat_rooms"
const val CHAT_MESSAGES = "messages"

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val messagesList: MutableList<Message> = mutableListOf()
    private lateinit var chatReference: DatabaseReference
    private lateinit var chatAdapter: ChatAdapter
    private var userId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)

        listenersInit()
        recyclerViewInit()
        val firebaseDB = Firebase.database
        chatReference = firebaseDB.getReference(CHAT_ROOMS_FIREBASE).child("1")
            .child(CHAT_MESSAGES)
        onMessageListChanged(chatReference)
    }

    private fun listenersInit() {
        binding.apply {
            back.setOnClickListener {
                finish()
            }

            menu.setOnClickListener {

            }

            add.setOnClickListener {
                userId = if (userId == 0) 1
                else 0
            }

            send.setOnClickListener {
                // TODO: При отправке сообщения, сначала оно отправляется на ВС, а потом, если оно
                // успешно сохранено, отправляется в Firebase.(Нужно чтобы в Firebase сохранялась
                // ссылка на картинку при ее наличии)
                val text = message.text.toString()
                if (text.isNotEmpty()) {
                    val msg = Message(text = text, time = System.currentTimeMillis(), userId)
                    chatReference.child(chatReference.push().key ?: "null").setValue(msg)
                    message.setText("")
                }
            }
        }

    }

    private fun recyclerViewInit() {
        binding.progress.isActivated = true
        chatAdapter = ChatAdapter(messagesList = messagesList, userId = userId)
        val layoutManager = LinearLayoutManager(this@ChatActivity)
        layoutManager.stackFromEnd = true
        binding.recyclerChat.layoutManager = layoutManager
        binding.recyclerChat.adapter = chatAdapter
    }

    private fun onMessageListChanged(firebaseDBReference: DatabaseReference) {
        firebaseDBReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                messagesList.clear()
                for (snapshot in snapshots.children) {
                    val message = snapshot.getValue(Message::class.java)
                    if (message != null) {
                        messagesList.add(message)
                    }
                }
                binding.progress.visibility = View.GONE
                binding.recyclerChat.adapter?.notifyItemChanged(messagesList.size -1)
                try {
                    binding.recyclerChat.smoothScrollToPosition(messagesList.size - 1)
                } catch (_: IllegalArgumentException) {}

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}