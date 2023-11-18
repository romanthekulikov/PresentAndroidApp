package com.example.present.activities.startPack.appModePack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.present.activities.startPack.formPack.FormActivity
import com.example.present.databinding.ActivityAppModeBinding

class AppModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.data
        binding.apply {
            make.setOnClickListener {
                val intent = Intent(this@AppModeActivity, FormActivity::class.java)
                startActivity(intent)
            }
            signIn.setOnClickListener {

            }
        }
    }
}