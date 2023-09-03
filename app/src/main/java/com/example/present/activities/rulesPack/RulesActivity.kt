package com.example.present.activities.rulesPack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.present.databinding.ActivityRulesBinding

class RulesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }
    }
}