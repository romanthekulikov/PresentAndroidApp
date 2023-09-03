package com.example.present.activities.welcomePack

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.present.activities.mainPack.MainActivity
import com.example.present.data.Pref
import com.example.present.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var welcomeVM: WelcomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        welcomeVM = ViewModelProvider(this, WelcomeViewModelFactory())[WelcomeViewModel::class.java]
        //welcomeVM.initCount()
        observationInit()

        binding.go.setOnClickListener {
            Pref(this).saveFirstOpening(false)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun observationInit() {
        welcomeVM.mutableFinishCount.observe(this) {
            if (it) {
                binding.content.visibility = View.VISIBLE
                binding.count.visibility = View.GONE
            }
        }

        welcomeVM.mutableStringCount.observe(this) {
            binding.count.text = it
        }
    }
}