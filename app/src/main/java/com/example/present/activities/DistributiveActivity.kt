package com.example.present.activities

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.present.R
import com.example.present.activities.startPack.appModePack.AppModeActivity
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

        if (beFirstOpen) {
            //TODO: Убрать заглушку
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDB(applicationContext)
                    val user = UserEntity(0, "Роман", "", "gadamob@gmail.com")
                    db.getUserDao().saveUser(user)
                } catch (_: SQLiteConstraintException) {

                }

            }
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, AppModeActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}