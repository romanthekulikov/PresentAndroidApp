package com.example.present.activities.gamePack.chatPack

import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.present.adapters.ChatAdapter
import com.example.present.databinding.ActivityChatBinding
import java.lang.NullPointerException

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter

    private lateinit var vm: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
        vm = ViewModelProvider(this, ChatViewModelFactory(this))[ChatViewModel::class.java]

        listenersInit()
        observersInit()
        recyclerViewInit()
        vm.getMessages()
    }

    private fun listenersInit() {
        binding.apply {
            back.setOnClickListener {
                finish()
            }

            menu.setOnClickListener {

            }

            add.setOnClickListener {
            }

            send.setOnClickListener {
                // TODO: При отправке сообщения, сначала оно отправляется на ВС, а потом, если оно
                // успешно сохранено, отправляется в Firebase.(Нужно чтобы в Firebase сохранялась
                // ссылка на картинку при ее наличии)
                val text = message.text.toString()
                if (text.isNotEmpty()) {
                    vm.sendMessageToFirebase(text)
                    message.setText("")
                }
            }
        }
    }

    private fun observersInit() {
        vm.messagesList.observe(this) {
            binding.progress.visibility = View.GONE
            try {
                chatAdapter.updateData(newList = it!!)
                binding.recyclerChat.smoothScrollToPosition(it.size - 1)
            } catch (_: IllegalArgumentException) {
            } catch (_: NullPointerException) {}
        }
    }

    private fun recyclerViewInit() {
        binding.progress.isActivated = true
        chatAdapter = ChatAdapter(messagesList = mutableListOf(), userId = vm.userId)
        val layoutManager = LinearLayoutManager(this@ChatActivity)
        layoutManager.stackFromEnd = true
        binding.recyclerChat.layoutManager = layoutManager
        binding.recyclerChat.adapter = chatAdapter
    }
}