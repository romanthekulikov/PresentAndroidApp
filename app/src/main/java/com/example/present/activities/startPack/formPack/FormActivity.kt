package com.example.present.activities.startPack.formPack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.present.adapters.FormAdapter
import com.example.present.data.Pref
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.databinding.ActivityFormBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var recyclerView: RecyclerView
    private var sawTutorial = false
    private var presentCount = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewInit()
        val pref = Pref(context = this)
        sawTutorial = pref.getSawPresentTutorial()
        if (!sawTutorial) {
            getDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
            pref.saveSawPresentTutorial(saw = true)
        }

        listenersInit()
    }

    private fun listenersInit() {
        binding.add.setOnClickListener {
            val intent = Intent(this@FormActivity, AddPresentActivity::class.java)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            finish()
        }
        binding.make.setOnClickListener {
            if (presentCount < 1) {
                getEmptyPresentDialog().show(
                    supportFragmentManager,
                    StringProvider.DIALOG_ERROR_TAG
                )
            } else {
                getSendDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
            }
        }
    }

    private fun recyclerViewInit() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDB(applicationContext)
            val presents = db.getFormItemDao().getAllFormItems()
            presentCount = presents.size
            CoroutineScope(Dispatchers.Main).launch {
                recyclerView = binding.recyclerView
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.adapter = FormAdapter(presents)
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

    private fun getSendDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.SEND_FORM_WARNING
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.YES)
        dialog.setNegativeButtonText(text = StringProvider.NO)
        dialog.setPositiveAction {
            CoroutineScope(Dispatchers.IO).launch {
                //TODO: Вот тут будет отправка и получение в ответ кода для входа
                val db = AppDatabase.getDB(applicationContext)
                db.getFormItemDao().deleteData()

                //TODO: Получим код и положим в intent:
                val key = "8961473"
                val intent = Intent(applicationContext, GetGameKeyActivity::class.java)
                intent.putExtra(IntentKeys.GAME_KEY, key)
                startActivity(intent)
                finish()
            }
        }
        return dialog
    }

    private fun getEmptyPresentDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.ERROR_SEND_EMPTY_PRESENT
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.DIALOG_UNDERSTAND_BUTTON)

        return dialog
    }
}