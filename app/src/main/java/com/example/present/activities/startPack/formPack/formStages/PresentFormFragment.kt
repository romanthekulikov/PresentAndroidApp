package com.example.present.activities.startPack.formPack.formStages

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.present.data.StringProvider
import com.example.present.data.models.PresentFormModel
import com.example.present.databinding.FrPresentFormBinding
import qrcode.QRCode
import java.io.File

class PresentFormFragment(
    private val onClick: PresentOnNextClick,
    private val presentForm: PresentFormModel?
) : Fragment() {
    companion object {
        var imageCompanion: Uri = Uri.EMPTY
        var bitmap: Bitmap? = null
        var qr: Bitmap? = null
    }


    private lateinit var _binding: FrPresentFormBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrPresentFormBinding.inflate(layoutInflater)


        fillFields()
        listenersInit()
        return _binding.root
    }

    private fun fillFields() {
        if (presentForm != null) {
            _binding.apply {
                try {
                    galleryImage.setImageURI(presentForm.image)
                } catch (_: Exception) {}
                congratulation.setText(presentForm.congratulationText)
                link.setText(presentForm.link)
                key.setText(presentForm.key)
                keyOpen.setText(presentForm.keyOpen)
                if (presentForm.qr != null) {
                    qrImage.setImageBitmap(presentForm.qr)
                    qrImage.visibility = View.VISIBLE
                    downloadQrLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun listenersInit() {
        _binding.complete.setOnClickListener {
            sentPresentToActivity()
        }

        _binding.image.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

        _binding.qr.setOnClickListener {
            generateQR()
        }

        _binding.downloadQr.setOnClickListener {
            try {
                bitmap?.let { it1 -> saveQR( it1) }
                Toast.makeText(requireContext(), StringProvider.QR_TOAST, Toast.LENGTH_LONG).show()
            } catch (_: Exception) {
            }
        }

        _binding.keyOpen.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                try {
                    if (_binding.keyOpen.text!!.isEmpty()) {
                        _binding.qrImage.visibility = View.GONE
                        _binding.downloadQr.visibility = View.GONE
                        bitmap = null
                    }
                } catch (_: Exception) {}

            }

        })
    }

    private fun generateQR() {
        val key = rand()
        val text =
            StringProvider.APP_DEEPLINK_BASE + StringProvider.APP_DEEPLINK_MAIN + StringProvider.ADD_CODE + key
        _binding.keyOpen.setText(text)
        val qrView = QRCode.ofSquares()
            .withBackgroundColor(Color.TRANSPARENT)
            .withSize(30)
            .build(text).renderToBytes()
        val qrDownLoad = QRCode.ofSquares()
            .withGradientColor(Color.parseColor("#3BF991"), Color.parseColor("#AB2DFA"))
            .withSize(30)
            .withBackgroundColor(Color.WHITE)
            .build(text).renderToBytes()
        bitmap = BitmapFactory.decodeByteArray(qrDownLoad, 0, qrDownLoad.size)
        qr = BitmapFactory.decodeByteArray(qrView, 0, qrView.size)

        _binding.qrImage.setImageBitmap(qr)
        _binding.qrImage.visibility = View.VISIBLE
        _binding.downloadQrLayout.visibility = View.VISIBLE
    }

    private fun saveQR(bitmap: Bitmap) {
        val file = File("/storage/emulated/0/Download", "qr.png")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    private fun rand(): Int {
        require(100000000 <= 999999999) { "Illegal Argument" }
        return (100000000..999999999).random()
    }

    private fun sentPresentToActivity() {
        var presentHasError = false
        val presentForm: PresentFormModel
        val congratulation = _binding.congratulation.text.toString()
        val link = _binding.link.text.toString()
        val key = _binding.key.text.toString()
        val keyOpen = _binding.keyOpen.text.toString()
        val img = imageCompanion
        if (congratulation.isEmpty()) {
            presentHasError = true
            _binding.congratulation.error = StringProvider.ERROR_CONGRATULATION_PRESENT
        }
        if (link.isEmpty()) {
            presentHasError = true
            _binding.link.error = StringProvider.ERROR_LINK_PRESENT
        }
        if (key.isEmpty()) {
            presentHasError = true
            _binding.key.error = StringProvider.ERROR_KEY_PRESENT
        }
        if (keyOpen.isEmpty()) {
            presentHasError = true
            _binding.keyOpen.error = StringProvider.ERROR_KEY_PRESENT
        }

        if (!presentHasError) {
            presentForm = PresentFormModel(congratulation, link, key, keyOpen, img, bitmap)

            onClick.onCompleteClickPresent(presentForm)
        }
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                if (imgUri != null) {
                    _binding.galleryImage.setImageURI(imgUri)
                    this.presentForm?.image = imgUri
                    imageCompanion = imgUri
                }
            }
        }

    override fun onDestroy() {
        val presentForm: PresentFormModel
        val congratulation = _binding.congratulation.text.toString()
        val link = _binding.link.text.toString()
        val key = _binding.key.text.toString()
        val keyOpen = _binding.keyOpen.text.toString()
        presentForm = PresentFormModel(congratulation, link, key, keyOpen, imageCompanion, qr)
        onClick.onBack(presentForm)
        super.onDestroy()
    }

    interface PresentOnNextClick {
        fun onCompleteClickPresent(presentForm: PresentFormModel)
        fun onBack(presentForm: PresentFormModel)
    }
}