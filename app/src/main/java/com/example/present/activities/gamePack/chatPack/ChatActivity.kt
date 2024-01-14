package com.example.present.activities.gamePack.chatPack

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.present.adapters.ChatAdapter
import com.example.present.data.database.AppDatabase
import com.example.present.data.models.Message
import com.example.present.databinding.ActivityChatBinding
import com.example.present.dialog.ChatItemDialog
import com.example.present.remote.ApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChatActivity : AppCompatActivity(), ChatAdapter.ClickListener {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private var textSize: Int = 12
    private var chatId = -1

    private lateinit var vm: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
        chatId = intent.extras?.getInt("chatId")!!
        val userId = intent.extras?.getInt("userId")!!
        vm = ViewModelProvider(this, ChatViewModelFactory(this, chatId))[ChatViewModel::class.java]
        vm.userId = userId

        applyChatSettings()
        listenersInit()
        observersInit()
        vm.getMessages()
    }

    private fun applyChatSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            val gameDao = AppDatabase.getDB(this@ChatActivity).getGameDao()
            val userDao = AppDatabase.getDB(this@ChatActivity).getUserDao()
            val user = userDao.getUser()!!
            val game = gameDao.getLastGame()!!
            val userApi = ApiProvider.userApi
            var appenderId = game.idAdmin
            if (game.idAdmin == user.id) {
                appenderId = game.idUser
            }
            val appender = userApi.getUser(appenderId).execute().body()
            val chatDao = AppDatabase.getDB(this@ChatActivity).getChatDao()
            val chat = chatDao.getChat()
            CoroutineScope(Dispatchers.Main).launch {
                if (chat.bgColor != "default") {
                    try {
                        binding.bg.setBackgroundColor(Color.parseColor(chat.bgColor))
                    } catch (e: Exception) {
                        Toast.makeText(this@ChatActivity, "Не удалось изменить фон", Toast.LENGTH_LONG).show()
                    }
                }
                if (chat.bgImg != "default") {
                    try {
                        Glide.with(this@ChatActivity).load(chat.bgImg).into(binding.bgImage)
                    } catch (e: Exception) {
                        Toast.makeText(this@ChatActivity, "Не удалось изменить фон", Toast.LENGTH_LONG).show()
                    }
                }
                Glide.with(this@ChatActivity).load(appender?.icon).circleCrop().into(binding.menIcon)
                binding.appenderName.text = appender?.name
                textSize = chat.textSize
                recyclerViewInit()
            }

        }
    }

    private fun listenersInit() {
        binding.apply {
            back.setOnClickListener {
                finish()
            }

            menu.setOnClickListener {
                if (!vm.searching.value!!) {
                    chatAdapter.updateData(vm.messagesList.value!!)
                    vm.searching.value = true
                    binding.menIcon.visibility = View.GONE
                    binding.appenderName.visibility = View.GONE
                    binding.search.visibility = View.VISIBLE
                } else {
                    vm.searching.value = false
                    doSearch()
                    binding.menIcon.visibility = View.VISIBLE
                    binding.appenderName.visibility = View.VISIBLE
                    binding.search.visibility = View.GONE
                }

            }

            binding.search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (binding.search.text.toString().isEmpty()) {
                        binding.menIcon.visibility = View.VISIBLE
                        binding.appenderName.visibility = View.VISIBLE
                        binding.search.visibility = View.GONE
                    }
                }

            })

            send.setOnClickListener {
                val text = message.text.toString()
                if (text.isNotEmpty()) {
                    if (vm.isTextEdit) {
                        val message =
                            vm.messagesList.value?.find { it.messageId == vm.editableMessageId }
                        try {
                            if (message!!.text != text) {
                                message.isEdit = true
                                message.text = text
                                CoroutineScope(Dispatchers.IO).launch {
                                    val chatApi = ApiProvider.chatApi
                                    chatApi.saveMessage(vm.userId, chatId, message.text!!, message.replayPosition!!).execute().body()
                                    CoroutineScope(Dispatchers.Main).launch {
                                        vm.editMessage(message)
                                        vm.isTextEdit = false
                                    }
                                }

                            }
                        } catch (_: Exception) {}
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            val chatApi = ApiProvider.chatApi
                            chatApi.saveMessage(vm.userId, chatId, text, null).execute().body()
                            CoroutineScope(Dispatchers.Main).launch {
                                vm.sendMessageToFirebase(text)
                            }
                        }

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
                }

            })
            removeReplay.setOnClickListener {
                vm.replayId = null
                replayLayout.visibility = View.GONE
            }
        }
    }

    private fun doSearch() {
        val substring = binding.search.text.toString()
        val list = vm.messagesList.value!!
        val newList = mutableListOf<Message>()
        for (item in list) {
            if (substring in item.text!!.toLowerCase()!!) {
                newList.add(item)
            }
        }
        chatAdapter.updateData(newList)
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
        chatAdapter = ChatAdapter(messagesList = mutableListOf(), userId = vm.userId, this, textSize)
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