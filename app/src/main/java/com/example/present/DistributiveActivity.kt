package com.example.present

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.present.data.Pref
import com.example.present.welcomePack.WelcomeActivity

class DistributiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributive)

        val beFirst = Pref(context = applicationContext).getFirstOpening()

        if (beFirst) {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}