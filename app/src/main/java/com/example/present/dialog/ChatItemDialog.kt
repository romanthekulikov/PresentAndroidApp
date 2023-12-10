package com.example.present.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import com.example.present.databinding.ChatItemDialogBinding


class ChatItemDialog(
    context: Context,
    private val touchX: Int,
    private val touchY: Int,
    private val gravity: Int,
    private val messageActions: SetAction,
) : Dialog(context) {
    private lateinit var binding: ChatItemDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatItemDialogBinding.inflate(layoutInflater)
        binding.apply {
            replay.setOnClickListener {
                messageActions.replayAction()
                dismiss()
            }
            edit.setOnClickListener {
                messageActions.editAction()
                dismiss()
            }
            delete.setOnClickListener {
                messageActions.deleteAction()
                dismiss()
            }
        }
        val layoutParam = window!!.attributes
        layoutParam.gravity = Gravity.TOP or gravity
        layoutParam.x = touchX
        layoutParam.y = touchY
        window?.attributes = layoutParam
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
        setContentView(binding.root)
    }

    interface SetAction {
        fun replayAction()
        fun editAction()
        fun deleteAction()
    }
}