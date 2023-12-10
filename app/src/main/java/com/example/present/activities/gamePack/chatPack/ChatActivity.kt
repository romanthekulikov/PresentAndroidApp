package com.example.present.activities.gamePack.chatPack

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.present.adapters.ChatAdapter
import com.example.present.data.models.Message
import com.example.present.databinding.ActivityChatBinding
import com.example.present.dialog.ChatItemDialog


class ChatActivity : AppCompatActivity(), ChatAdapter.ClickListener {
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
                    if (vm.isTextEdit) {
                        val message =
                            vm.messagesList.value?.find { it.messageId == vm.editableMessageId }
                        try {
                            if (message!!.text != text) {
                                message.isEdit = true
                                message.text = text
                                vm.editMessage(message)
                                vm.isTextEdit = false
                            }
                        } catch (_: Exception) {}
                    } else {
                        vm.sendMessageToFirebase(text)
                    }
                }
                message.setText("")
                replayLayout.visibility = View.GONE
                vm.replayId = null
            }
            message.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    if (message.text.isEmpty()) add.visibility = View.VISIBLE
                    else add.visibility = View.GONE
                }

            })
            removeReplay.setOnClickListener {
                vm.replayId = null
                replayLayout.visibility = View.GONE
            }
        }
    }

    private fun observersInit() {
        vm.messagesList.observe(this) {
            val chatState = binding.recyclerChat.layoutManager?.onSaveInstanceState()
            try {
                val newItemCount = it!!.size - chatAdapter.itemCount
                chatAdapter.updateData(newList = it)
                if (newItemCount > 0) {
                    binding.progress.visibility = View.GONE
                    binding.recyclerChat.smoothScrollToPosition(it.size - 1)
                }
            } catch (_: Exception) {}
            binding.recyclerChat.layoutManager?.onRestoreInstanceState(chatState)
        }
    }

    private fun recyclerViewInit() {
        binding.progress.isActivated = true
        chatAdapter = ChatAdapter(messagesList = mutableListOf(), userId = vm.userId, this)
        val layoutManager = LinearLayoutManager(this@ChatActivity)
        layoutManager.stackFromEnd = true
        binding.recyclerChat.layoutManager = layoutManager
        binding.recyclerChat.adapter = chatAdapter
        addTouchHelper()
    }

    private fun addTouchHelper() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val message = vm.messagesList.value?.get(viewHolder.adapterPosition)
                binding.recyclerChat.adapter?.notifyItemChanged(viewHolder.adapterPosition)
                binding.replayText.text = message!!.text
                binding.replayUserName.text = "Пользователь${message.userId}"
                binding.replayLayout.visibility = View.VISIBLE
                vm.replayId = message.messageId
            }
        })

        helper.attachToRecyclerView(binding.recyclerChat)
    }

    override fun clickOnReplayMessage(position: Int) {
        try {
            binding.recyclerChat.smoothScrollToPosition(position)
        } catch (_: IllegalArgumentException) {
        }
    }

    override fun longClickListener(message: Message, touchX: Int, touchY: Int) {
        val gravityDialog = if (message.userId == vm.userId) Gravity.START else Gravity.END
        val dialog = ChatItemDialog(
            context = this,
            touchX = touchX,
            touchY = touchY,
            gravity = gravityDialog,
            messageActions = object : ChatItemDialog.SetAction {
                override fun replayAction() {
                    vm.replayId = message.messageId
                    binding.apply {
                        replayLayout.visibility = View.VISIBLE
                        replayText.text = message.text
                        replayUserName.text = "Пользователь${message.userId}"
                    }

                }

                override fun editAction() {
                    vm.isTextEdit = true
                    vm.editableMessageId = message.messageId
                    binding.message.setText(message.text)
                }

                override fun deleteAction() {
                    vm.deleteMessage(messageId = message.messageId!!)
                }

            })
        dialog.show()
    }
}