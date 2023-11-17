package com.example.present.activities.startPack.formPack.formStages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.present.data.models.PresentFormModel
import com.example.present.databinding.FragmentPresentFormBinding

class PresentFormFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = PresentFormFragment()
        var present = PresentFormModel("", "", "", Uri.EMPTY)
    }

    private lateinit var _binding: FragmentPresentFormBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPresentFormBinding.inflate(layoutInflater)

        _binding.image.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }
        listenersInit()

        return _binding.root
    }

    private fun listenersInit() {
        _binding.congratulation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                present.congratulationText = _binding.congratulation.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        _binding.link.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                present.link = _binding.link.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        _binding.key.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                present.key = _binding.key.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                _binding.galleryImage.setImageURI(imgUri)
                if (imgUri != null) {
                    present.image = imgUri
                }
            }
        }
}