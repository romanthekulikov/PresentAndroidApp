package com.example.present.activities.gamePack.getPresentPack

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
const val APOTHECARY_PRESENT = 1
const val MASSAGE_PRESENT = 2

class GetPresentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetPresentBinding
    private var presentId = OZON_PRESENT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetPresentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presentId = intent.getIntExtra(IntentKeys.PROGRESS_KEY, OZON_PRESENT)

        binding.apply {
            when (presentId) {
                OZON_PRESENT -> {
                    congratulation.text = resources.getText(R.string.first_congratulation)
                    certificateImage.setImageResource(R.drawable.ozon)
                    open.setOnClickListener {
                        writeToClipBoard()
                        val task = {
                            val uri = Uri.parse(StringProvider.OZON_REDIRECT_URL)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                        getDialog(task).show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
                    }
                }

                APOTHECARY_PRESENT -> {
                    congratulation.text = resources.getText(R.string.second_congratulation)
                }

                MASSAGE_PRESENT -> {
                    congratulation.text = resources.getText(R.string.third_congratulation)
                }
            }

            binding.back.setOnClickListener {
                finish()
            }
        }

    }

    private fun writeToClipBoard() {
        val clipboard: ClipboardManager =
            applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", StringProvider.OZON_CERTIFICATE_KEY)
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