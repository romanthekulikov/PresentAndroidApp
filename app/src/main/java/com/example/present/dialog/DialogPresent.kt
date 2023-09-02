package com.example.present.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.example.present.databinding.DialogBinding

class DialogPresent : DialogFragment() {

    private lateinit var binding: DialogBinding
    private var positiveText: String = "Да"
    private var negativeText: String = "Нет"

    private var positiveAction: () -> Unit = {}
    private var negativeAction: () -> Unit = {}

    private var messageText = ""

    private lateinit var positiveButton: AppCompatButton
    private lateinit var negativeButton: AppCompatButton
    private lateinit var message: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBindingItems()
        setBindingProperties()

        binding.positiveButton.setOnClickListener {
            positiveAction()
            dismiss()
        }

        binding.negativeButton.setOnClickListener {
            negativeAction()
            dismiss()
        }

        if (negativeText == "Нет") {
            negativeButton.visibility = View.GONE
        }

        return binding.root
    }

    fun setMessage(text: String) {
        messageText = text
    }

    fun setPositiveButtonText(text: String) {
        positiveText = text
    }

    fun setNegativeButtonText(text: String) {
        negativeText = text
    }

    fun setPositiveAction(action: () -> Unit) {
        positiveAction = action
    }

    private fun setBindingItems() {
        binding = DialogBinding.inflate(layoutInflater)
        positiveButton = binding.positiveButton
        negativeButton = binding.negativeButton
        message = binding.message
    }

    private fun setBindingProperties() {
        positiveButton.text = positiveText
        negativeButton.text = negativeText
        message.text = messageText
    }
}