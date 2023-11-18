package com.example.present.activities.startPack.formPack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.present.adapters.FormAdapter
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.databinding.ActivityFormBinding
import com.example.present.dialog.DialogPresent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerViewInit()
        getDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
        binding.add.setOnClickListener {
            val intent = Intent(this@FormActivity, AddPresentActivity::class.java)
            startActivity(intent)
        }

    }

    private fun recyclerViewInit() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDB(applicationContext)
            val presents = db.getFormItemDao().getAllFormItems()
            CoroutineScope(Dispatchers.Main).launch {
                val recyclerView = binding.recyclerView
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.adapter = FormAdapter(presents, applicationContext)
            }
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