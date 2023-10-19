package com.example.present.activities.getPresentPack

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.present.R
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityGetPresentBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys

const val OZON_PRESENT = 0
const val YANDEX_PRESENT = 1
const val GOLDEN_APPLE_PRESENT = 2

class GetPresentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetPresentBinding
    private var presentId = OZON_PRESENT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetPresentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presentId = intent.getIntExtra(IntentKeys.PROGRESS_KEY, OZON_PRESENT)

        val giftImage = when(presentId) {
            OZON_PRESENT -> R.drawable.ozon
            YANDEX_PRESENT -> R.drawable.yandex
            GOLDEN_APPLE_PRESENT -> R.drawable.golden_apple
            else -> R.drawable.ozon
        }
        val key = StringProvider.keyMap[presentId]!!

        binding.apply {
            congratulation.text = StringProvider.congratulationsWithPresent[presentId]
            certificateImage.setImageResource(giftImage)
            open.setOnClickListener {
                writeKeyToClipBoard(key)
                val task = {
                    val uri = Uri.parse(StringProvider.redirectUrls[presentId])
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                getDialog(task).show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
            }
        }

        binding.back.setOnClickListener {
            finish()
        }

    }

    private fun writeKeyToClipBoard(key: String) {
        val clipboard: ClipboardManager =
            applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", key)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, StringProvider.COPY_FINISHED, Toast.LENGTH_SHORT).show()
    }

    private fun getDialog(task: () -> Unit): DialogPresent {
        val dialog = DialogPresent()
        dialog.setMessage(StringProvider.REDIRECT_WARNING)
        dialog.setPositiveButtonText(StringProvider.GO)
        dialog.setPositiveAction(task)

        return dialog
    }
}