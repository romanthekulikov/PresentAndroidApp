package com.example.present.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.present.R
import com.example.present.activities.gamePack.mainPack.MainActivity
import com.example.present.activities.startPack.appModePack.AppModeActivity
import com.example.present.activities.startPack.authorizationPack.AuthorizationActivity
import com.example.present.activities.startPack.welcomePack.WelcomeActivity
import com.example.present.data.Pref
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.domain.IntentKeys
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class DistributiveActivity : AppCompatActivity() {
    private var taskActivity = ""
    private var taskArg = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(StringProvider.API)  //Инициализация API для Яндекс карт
        setContentView(R.layout.activity_distributive)

        parseTask()
        val beFirstOpen = Pref(context = applicationContext).getFirstOpening()
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDB(applicationContext)
            val user = db.getUserDao().getUser()
            val game = db.getGameDao().getLastGame()
            val intent: Intent = if (beFirstOpen) {
                Intent(applicationContext, WelcomeActivity::class.java)
            } else if (game != null) {
                Intent(applicationContext, MainActivity::class.java)
            } else if (user != null) {
                Intent(applicationContext, AppModeActivity::class.java)
            } else {
                Intent(applicationContext, AuthorizationActivity::class.java)
            }
            intent.putExtra(IntentKeys.TASK_ACTIVITY, taskActivity)
            intent.putExtra(IntentKeys.TASK_ARG, taskArg)
            startActivity(intent)
        }

        finish()
    }

    private fun parseTask() {
        try {
            val data = intent.data.toString()
            val arg = data.split("://")[1]
            val activity = arg.split("/")[1].split("&")[0]
            val variable = arg.split("/")[1].split("&")[1].split("=")[1]
            taskActivity = activity
            taskArg = variable
        } catch (_: Exception) {
        }
    }
}