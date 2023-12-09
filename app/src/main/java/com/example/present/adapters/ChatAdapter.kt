package com.example.present.adapters

import android.view.Gravity
import android.view.LayoutInflater
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
class ChatAdapter(private var messagesList: List<Message>, private val userId: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var bindingUserMessage: ChatMessageUserItemBinding
    private lateinit var bindingAppenderMessage: ChatMessageAppenderItemBinding

    class UserMessageViewHolder(private val binding: ChatMessageUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.END
            binding.mainLayout.layoutParams = params

            binding.text.text = item.text
            val formatter = SimpleDateFormat("HH:mm", Locale("ru"))
            val time = formatter.format(item.time)
            binding.time.text = time
        }
    }

    class AppenderMessageViewHolder(private val binding: ChatMessageAppenderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.text.text = item.text
            val formatter = SimpleDateFormat("HH:mm", Locale("ru"))
            val time = formatter.format(item.time)
            binding.time.text = time
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        bindingUserMessage = ChatMessageUserItemBinding.inflate(LayoutInflater.from(parent.context))
        bindingAppenderMessage = ChatMessageAppenderItemBinding.inflate(LayoutInflater.from(parent.context))
        return when (viewType) {
            USER_TYPE -> UserMessageViewHolder(bindingUserMessage)
            else -> AppenderMessageViewHolder(bindingAppenderMessage)
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
        if (newItemCount >= 0) notifyItemRangeChanged(currentSize, newItemCount)
    }
}