package com.example.present.activities.startPack.authorizationPack

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.present.activities.startPack.appModePack.AppModeActivity
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.UserEntity
import com.example.present.databinding.ActivityAuthorizationBinding
import com.example.present.domain.IntentKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorizationBinding
    private var taskActivity = ""
    private var taskArg = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        if (data != null) {
            taskActivity = data.getString(IntentKeys.TASK_ACTIVITY, "")
            taskArg = data.getString(IntentKeys.TASK_ARG, "")
        }

        binding.enter.setOnClickListener {
            if (!fieldsIncorrect()) {
                authorization()
            }
        }
    }

    private fun fieldsIncorrect(): Boolean {
        var authHasError = false
        if (binding.email.text.isNullOrEmpty() || !isValidEmail(binding.email.text.toString())) {
            authHasError = true
            binding.email.error = StringProvider.INVALID_EMAIL
        }
        if (binding.password.text.isNullOrEmpty()) {
            authHasError = true
            binding.password.error = StringProvider.INVALID_PASSWORD
        }

        return authHasError
    }

    private fun authorization() {
        //TODO: Тут отправляются данные на проверку
        //TODO: Если приходит успешно -- пропускаем, если ошибка, то по ошибке смотрим гле ошибся пользователь
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDB(applicationContext)
            val user = UserEntity(0, "Роман", "", binding.email.text.toString())
            db.getUserDao().saveUser(user)
            CoroutineScope(Dispatchers.Main).launch {
                //TODO: Пока на экран выбора режима, далее нужно подгрузить инфй и переводить на главный экран
                val intent = Intent(applicationContext, AppModeActivity::class.java)
                intent.putExtra(IntentKeys.TASK_ACTIVITY, taskActivity)
                intent.putExtra(IntentKeys.TASK_ARG, taskArg)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun isValidEmail(target: String): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}