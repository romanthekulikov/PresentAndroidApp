package com.example.present.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.present.data.models.Message
import com.example.present.databinding.ChatMessageAppenderItemBinding
import com.example.present.databinding.ChatMessageUserItemBinding
import java.text.SimpleDateFormat
import java.util.Locale


const val USER_TYPE = 0
const val APPENDER_TYPE = 1

class ChatAdapter(
    private var messagesList: List<Message>,
    private val userId: Int,
    private val listener: ClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var bindingUserMessage: ChatMessageUserItemBinding
    private lateinit var bindingAppenderMessage: ChatMessageAppenderItemBinding

    class UserMessageViewHolder(
        private val binding: ChatMessageUserItemBinding,
        private val listener: ClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.END
            binding.mainLayout.layoutParams = params
            binding.apply {
                text.text = item.text
                val formatter = SimpleDateFormat("HH:mm", Locale("ru"))
                val timeSend = formatter.format(item.time)
                time.text = timeSend
                if (!item.replayId.isNullOrEmpty()) {
                    replayLayout.visibility = View.VISIBLE
                    replayText.text = item.replayText
                    replayName.text = "Пользователь ${item.replayUserId}"
                } else {
                    replayLayout.visibility = View.GONE
                }
                if (item.isEdit) {
                    binding.editedMark.visibility = View.VISIBLE
                } else {
                    binding.editedMark.visibility = View.GONE
                }
                replayLayout.setOnClickListener {
                    item.replayPosition?.let { it1 -> listener.clickOnReplayMessage(it1) }
                }
                mainLayout.setOnLongClickListener {
                    val location = IntArray(2)
                    it.getLocationOnScreen(location)
                    val x = location[0]
                    val y = location[1]
                    listener.longClickListener(item, x, y)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    class AppenderMessageViewHolder(
        private val binding: ChatMessageAppenderItemBinding,
        private val listener: ClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.apply {
                text.text = item.text
                val formatter = SimpleDateFormat("HH:mm", Locale("ru"))
                val timeSend = formatter.format(item.time)
                time.text = timeSend
                if (!item.replayId.isNullOrEmpty()) {
                    replayLayout.visibility = View.VISIBLE
                    replayText.text = item.replayText
                    replayName.text = "Пользователь ${item.replayUserId}"
                } else {
                    replayLayout.visibility = View.GONE
                }
                if (item.isEdit) {
                    binding.editedMark.visibility = View.VISIBLE
                } else {
                    binding.editedMark.visibility = View.GONE
                }
                replayLayout.setOnClickListener {
                    item.replayPosition?.let { it1 -> listener.clickOnReplayMessage(it1) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        bindingUserMessage = ChatMessageUserItemBinding.inflate(LayoutInflater.from(parent.context))
        bindingAppenderMessage =
            ChatMessageAppenderItemBinding.inflate(LayoutInflater.from(parent.context))
        return when (viewType) {
            USER_TYPE -> UserMessageViewHolder(
                binding = bindingUserMessage,
                listener = listener
            )

            else -> AppenderMessageViewHolder(
                binding = bindingAppenderMessage,
                listener = listener
            )
        }
    }

    override fun getItemCount(): Int {
        return messagesList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: Message = messagesList[position]
        when (holder.itemViewType) {
            USER_TYPE -> (holder as UserMessageViewHolder).bind(item)
            else -> (holder as AppenderMessageViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messagesList[position].userId == userId) {
            return USER_TYPE
        }
        return APPENDER_TYPE
    }

    fun updateData(newList: List<Message>) {
        val currentSize = messagesList.size
        val newItemCount = newList.size - currentSize
        messagesList = newList
        if (newItemCount > 0) notifyItemRangeChanged(currentSize, newItemCount)
        else {
            notifyItemRangeRemoved(0, currentSize)
            notifyItemRangeInserted(0, messagesList.size)
        }
    }

    interface ClickListener {
        fun clickOnReplayMessage(position: Int)
        fun longClickListener(message: Message, touchX: Int, touchY: Int)
    }
}