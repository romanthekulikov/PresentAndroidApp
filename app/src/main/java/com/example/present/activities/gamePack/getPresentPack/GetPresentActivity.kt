package com.example.present.activities.gamePack.getPresentPack

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.present.R
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.databinding.ActivityGetPresentBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys
import com.example.present.remote.ApiProvider
import com.google.common.base.MoreObjects.ToStringHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val OZON_PRESENT = 0
const val APOTHECARY_PRESENT = 1
const val MASSAGE_PRESENT = 2

class GetPresentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetPresentBinding
    private var presentId = OZON_PRESENT
    private var link = ""
    private var presentKeyOpen = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetPresentBinding.inflate(layoutInflater)
        var presentImg = ""

        setContentView(binding.root)
        binding.progress.isActivated = true
        presentId = intent.getIntExtra(IntentKeys.PROGRESS_KEY, OZON_PRESENT)
        CoroutineScope(Dispatchers.IO).launch {
            val presentDao = AppDatabase.getDB(this@GetPresentActivity).getPresentDao()
            val gameApi = ApiProvider.gameApi
            val present = presentDao.getPresentById(presentId)
            val response = gameApi.getPresentKey(presentId).execute().body()
            presentImg = presentDao.getPresentById(presentId).image
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    binding.congratulation.text = present.congratulation
                    presentKeyOpen = response!!.key_open
                    link = present.link
                    Glide.with(this@GetPresentActivity).load(presentImg).into(binding.certificateImage)
                    binding.progress.visibility = View.GONE
                } catch (e: Exception) {
                    Toast.makeText(this@GetPresentActivity, "Что-то пошло не так(", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.apply {
            back.setOnClickListener {
                finish()
            }

            open.setOnClickListener {
                writeKeyToClipBoard()
                getDialog().show(supportFragmentManager, "")
            }
        }

    }

    private fun writeKeyToClipBoard() {
        val clipboard: ClipboardManager =
            applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", presentKeyOpen)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, StringProvider.COPY_FINISHED, Toast.LENGTH_SHORT).show()
    }

    private fun getDialog(): DialogPresent {
        val dialog = DialogPresent()
        dialog.setMessage(StringProvider.REDIRECT_WARNING)
        dialog.setPositiveButtonText(StringProvider.GO)
        dialog.setPositiveAction {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(link))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
            }

        }

        return dialog
    }
}