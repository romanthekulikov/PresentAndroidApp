package com.example.present.presentPack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.present.R
import com.example.present.databinding.ActivityPresentBinding
import com.example.present.domain.IntentKeys
import com.example.present.getPresentPack.GetPresentActivity

class PresentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPresentBinding
    private lateinit var presentVM: PresentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presentVM =
            ViewModelProvider(this, PresentViewModelFactory(this))[PresentViewModel::class.java]
        presentVM.getData()
        buttonInit()
        listenersInit()
    }

    private fun buttonInit() {

        when (presentVM.progress.value) {
            0 -> binding.apply {
                firstPresent.setBackgroundColor(resources.getColor(R.color.gray))
                secondPresent.setBackgroundColor(resources.getColor(R.color.gray))
                thirdPresent.setBackgroundColor(resources.getColor(R.color.gray))
            }

            1 -> binding.apply {
                secondPresent.setBackgroundColor(resources.getColor(R.color.gray))
                thirdPresent.setBackgroundColor(resources.getColor(R.color.gray))
            }

            2 -> binding.apply {
                thirdPresent.setBackgroundColor(resources.getColor(R.color.gray))
            }
        }
    }

    private fun listenersInit() {
        val progress = presentVM.progress.value!!
        val intent = Intent(this, GetPresentActivity::class.java)
        binding.apply {
            firstPresent.setOnClickListener {
                if (progress > -1) {
                    intent.putExtra(IntentKeys.PROGRESS_KEY, 0)
                    startActivity(intent)
                }
            }

            secondPresent.setOnClickListener {
                if (progress > 0) {
                    intent.putExtra(IntentKeys.PROGRESS_KEY, 1)
                    startActivity(intent)
                }
            }

            thirdPresent.setOnClickListener {
                if (progress > 1) {
                    intent.putExtra(IntentKeys.PROGRESS_KEY, 2)
                    startActivity(intent)
                }
            }

            back.setOnClickListener {
                finish()
            }
        }
    }
}