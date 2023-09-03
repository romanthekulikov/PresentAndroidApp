package com.example.present.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.present.activities.mainPack.MainActivity
import com.example.present.R
import com.example.present.data.Pref
import com.example.present.data.StringProvider
import com.example.present.activities.welcomePack.WelcomeActivity
import com.yandex.mapkit.MapKitFactory

class DistributiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(StringProvider.API)  //Инициализация API для Яндекс карт
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