package com.example.present.activities.startPack.formPack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.present.adapters.FormAdapter
import com.example.present.data.Pref
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.FormItemEntity
import com.example.present.databinding.ActivityFormBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys
import com.example.present.remote.ApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var recyclerView: RecyclerView
    private var presents: MutableList<FormItemEntity> = mutableListOf()
    private var sawTutorial = false


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progress.isActivated = true
        recyclerViewInit()
        val pref = Pref(context = this)
        sawTutorial = pref.getSawPresentTutorial()
        if (!sawTutorial) {
            getDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
            pref.saveSawPresentTutorial(saw = true)
        }
        addTouchHelper()
        listenersInit()
    }

    private fun listenersInit() {
        binding.add.setOnClickListener {
            val intent = Intent(this@FormActivity, AddPresentActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.back.setOnClickListener {
            finish()
        }
        binding.make.setOnClickListener {
            if (presents.size < 1) {
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
            val dbList: List<FormItemEntity> = db.getFormItemDao().getAllFormItems()
            for (item in dbList) {
                presents.add(item)
            }
            CoroutineScope(Dispatchers.Main).launch {
                recyclerView = binding.recyclerView
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.adapter = FormAdapter(presents, this@FormActivity)
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
            binding.progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val key = sendGame()
                val db = AppDatabase.getDB(applicationContext)
                db.getFormItemDao().deleteData()
                if (key != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val intent = Intent(applicationContext, GetGameKeyActivity::class.java)
                        intent.putExtra(IntentKeys.GAME_KEY, key)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        return dialog
    }

    private fun sendGame(): String? {
        val db = AppDatabase.getDB(applicationContext)
        val userId = db.getUserDao().getUser()!!.id
        val json = makePresentJson(userId)
        val gameApi = ApiProvider.gameApi
        val createGameResponse = gameApi.createGame(json = json).execute().body()

        return try {
            if (createGameResponse!!.error == null) {
                createGameResponse.keyEnter
            } else {
                null
            }
        } catch (e: Exception) { null }

    }

    private fun makePresentJson(idAdmin: Int): String {
        var json = ""
        json += "{\"id_admin\": $idAdmin, " +
                "\"stages\": ["
        for (i in presents.indices) {
            json += "{\"text_stage\": \"${presents[i].textStage}\", " +
                    "\"hint_text\": \"${presents[i].hintText}\", " +
                    "\"long\": ${presents[i].longitude}, " +
                    "\"lat\": ${presents[i].latitude}, " +
                    "\"key_present_game\": \"${presents[i].keyOpen}\"}"
            if (i < presents.size - 1) {
                json += ","
            }
        }
        json += "], \"presents\": ["
        for (i in presents.indices) {
            json += "{\"present_text\": \"${presents[i].congratulation}\", " +
                    "\"present_img\": \"${presents[i].presentImg}\"," +
                    "\"key\": \"${presents[i].key}\"," +
                    "\"redirect_link\": \"${presents[i].link}\"}"
            if (i < presents.size - 1) {
                json += ","
            }
        }
        json += "]}"
        return json
    }

    private fun getEmptyPresentDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.ERROR_SEND_EMPTY_PRESENT
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.DIALOG_UNDERSTAND_BUTTON)

        return dialog
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
                val present = presents[viewHolder.adapterPosition]
                getRemovePresentDialog {
                    CoroutineScope(Dispatchers.IO).launch {
                        val formDao = AppDatabase.getDB(this@FormActivity).getFormItemDao()
                        try {
                            formDao.deletePresent(present.idStage)
                            CoroutineScope(Dispatchers.Main).launch {
                                presents.remove(present)
                                binding.recyclerView.adapter?.notifyItemChanged(viewHolder.adapterPosition)
                            }
                        } catch (e: Exception) {
                            binding.recyclerView.adapter?.notifyDataSetChanged()
                            Toast.makeText(this@FormActivity, "Не получилось удалить подарок", Toast.LENGTH_LONG).show()
                        }

                    }
                }.show(supportFragmentManager, "")

            }
        })

        helper.attachToRecyclerView(binding.recyclerView)
    }

    private fun getRemovePresentDialog(action: () -> Unit): DialogPresent {
        val dialog = DialogPresent()
        val message = "Удалить подарок?"
        dialog.setMessage(text = message)
        dialog.setNegativeButtonText(text = StringProvider.NO)
        dialog.setPositiveButtonText(text = StringProvider.YES)
        dialog.setPositiveAction { action() }

        return dialog
    }
}