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
import com.example.present.remote.ApiProvider
import com.google.common.base.MoreObjects.ToStringHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorizationBinding
    private var taskActivity = ""
    private var taskArg = ""
    private var isSignUp = false
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
                if (isSignUp) {
                    signUp()
                } else {
                    signIn()
                }

            }
        }
        binding.signUp.setOnClickListener {
            if (!isSignUp) {
                isSignUp = true
                binding.enterText.text = "Регистрация"
                binding.signUp.text = "Авторизация"
            } else {
                isSignUp = false
                binding.enterText.text = "Авторизация"
                binding.signUp.text = "Или пройти регистрацию"
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

    private fun signIn() {
        CoroutineScope(Dispatchers.IO).launch {
            val userApi = ApiProvider.userApi
            val authQuery = userApi.auth(email = binding.email.text.toString(), password = binding.password.text.toString())
            val response = authQuery.execute().body()
            try {
                if (response!!.error == null) {
                    val db = AppDatabase.getDB(applicationContext)
                    val user = UserEntity(response.userId, response.name, response.icon, response.email)
                    db.getUserDao().saveUser(user)
                    CoroutineScope(Dispatchers.Main).launch {
                        val intent = Intent(applicationContext, AppModeActivity::class.java)
                        intent.putExtra(IntentKeys.TASK_ACTIVITY, taskActivity)
                        intent.putExtra(IntentKeys.TASK_ARG, taskArg)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@AuthorizationActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@AuthorizationActivity, "Произошла проблема", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun signUp() {
        CoroutineScope(Dispatchers.IO).launch {
            val userApi = ApiProvider.userApi
            val regQuery = userApi.reg(email = binding.email.text.toString(), password = binding.password.text.toString())
            val response = regQuery.execute().body()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    if (response!!.error == 200) {
                        Toast.makeText(this@AuthorizationActivity, response.message, Toast.LENGTH_LONG).show()
                        isSignUp = false
                        binding.enterText.text = "Авторизация"
                        binding.signUp.text = "Или пройти регистрацию"
                    } else {
                        Toast.makeText(this@AuthorizationActivity, response.message, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AuthorizationActivity, "Произошла проблема", Toast.LENGTH_LONG).show()
                }
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