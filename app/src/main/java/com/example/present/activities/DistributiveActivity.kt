package com.example.present.activities

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.present.R
import com.example.present.activities.startPack.appModePack.AppModeActivity
import com.example.present.activities.startPack.authorizationPack.AuthorizationActivity
import com.example.present.activities.startPack.welcomePack.WelcomeActivity
import com.example.present.data.Pref
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.UserEntity
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class DistributiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(StringProvider.API)  //Инициализация API для Яндекс карт
        setContentView(R.layout.activity_distributive)

        val beFirstOpen = Pref(context = applicationContext).getFirstOpening()
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDB(applicationContext)
            val user = db.getUserDao().getUser()
            val intent: Intent = if (beFirstOpen) {
                Intent(applicationContext, WelcomeActivity::class.java)
            } else if (user != null) {
                Intent(applicationContext, AppModeActivity::class.java)
            } else {
                Intent(applicationContext, AuthorizationActivity::class.java)
            }
            startActivity(intent)
        }

        finish()
    }
}