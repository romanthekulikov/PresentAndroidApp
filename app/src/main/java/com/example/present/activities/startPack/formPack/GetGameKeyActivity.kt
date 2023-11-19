package com.example.present.activities.startPack.formPack

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import com.example.present.R
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityGetGameKeyBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys
import qrcode.QRCode
import java.io.File
import java.io.FileOutputStream

class GetGameKeyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetGameKeyBinding
    private lateinit var layoutAnimation: Animation
    private var key: String? = ""
    private var shareIsOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetGameKeyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        key = intent.extras?.getString(IntentKeys.GAME_KEY)
        binding.key.text = key
        binding.numbText.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        listenersInit()
        animationInit()

        addBackPressed()
    }

    private fun addBackPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                getDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
            }
        }
        onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    private fun animationInit() {
        layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.appear)
        binding.mainLayout.startAnimation(layoutAnimation)
        binding.mainLayout.translationY = -400f
        ViewCompat.animate(binding.mainLayout)
            .translationYBy(400f)
            .setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator()).setStartDelay(150)
            .withEndAction {
                ViewCompat.animate(binding.backLayout)
                    .setDuration(500)
                    .alphaBy(1f)
                    .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
                ViewCompat.animate(binding.shareLayout)
                    .setDuration(600)
                    .alphaBy(1f)
                    .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
            }
    }

    private fun listenersInit() {
        val beginShareViewRotation = binding.share.rotation
        binding.back.setOnClickListener {
            getDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
        }

        binding.key.setOnLongClickListener {
            writeToClipBoard(key)
            true
        }

        binding.share.setOnClickListener {
            ViewCompat.animate(binding.qrLayout)
                .setDuration(250)
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
            ViewCompat.animate(binding.numbLayout)
                .setDuration(200)
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150

            ViewCompat.animate(binding.share)
                .setDuration(250)
                .rotation(-50f)
                .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
            shareIsOpen = true
        }

        binding.layout.setOnClickListener {
            if (shareIsOpen) {
                ViewCompat.animate(binding.qrLayout)
                    .setDuration(250)
                    .scaleX(0f)
                    .scaleY(0f)
                    .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
                ViewCompat.animate(binding.numbLayout)
                    .setDuration(200)
                    .scaleX(0f)
                    .scaleY(0f)
                    .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150

                ViewCompat.animate(binding.share)
                    .setDuration(250)
                    .rotation(beginShareViewRotation)
                    .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
            }
            shareIsOpen = false
        }

        binding.numb.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, key)
                type = "text/plain"
            }
            try {
                startActivity(sendIntent)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(this, StringProvider.ERROR_SEND_MESSAGE, Toast.LENGTH_LONG).show()
            }
        }

        binding.qr.setOnClickListener {
            sendQR()
        }
    }

    private fun sendQR() {
        val text =
            StringProvider.APP_DEEPLINK_BASE + StringProvider.APP_DEEPLINK_GAME_AUTHORIZATION + StringProvider.ADD_CODE + key
        val qrDownLoad = QRCode.ofSquares()
            .withGradientColor(Color.parseColor("#3BF991"), Color.parseColor("#AB2DFA"))
            .withSize(30)
            .withBackgroundColor(Color.WHITE)
            .build(text).renderToBytes()

        val folder = File(cacheDir, "images")
        val uri: Uri?
        try {
            folder.mkdirs()
            val file = File(folder, "send_qr.png")
            val outputStream = FileOutputStream(file)
            BitmapFactory.decodeByteArray(qrDownLoad, 0, qrDownLoad.size)
                .compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            uri = FileProvider.getUriForFile(this, applicationContext.packageName, file)

            val intent = Intent()
            intent.apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/*"
            }

            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, StringProvider.ERROR_SEND_MESSAGE, Toast.LENGTH_LONG).show()
        }
    }

    private fun getDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.EXIT_GET_KEY
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.EXIT)
        dialog.setPositiveAction {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
            finish()
        }
        return dialog
    }

    private fun writeToClipBoard(str: String?) {
        val clipboard: ClipboardManager =
            applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", str)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, StringProvider.COPY_FINISHED, Toast.LENGTH_SHORT).show()
    }
}
