package com.example.present.activities.startPack.formPack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityFormBinding
import com.example.present.dialog.DialogPresent

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO: Инициализировать адаптер и наполнить его информацией из БД
        getDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)

        binding.add.setOnClickListener {
            val intent = Intent(this@FormActivity, AddPresentActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.FORM_MESSAGE
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.AGREE)

        return dialog
    }
}