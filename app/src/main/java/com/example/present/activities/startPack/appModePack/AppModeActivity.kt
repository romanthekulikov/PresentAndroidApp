package com.example.present.activities.startPack.appModePack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.present.activities.profilePack.ProfileActivity
import com.example.present.activities.startPack.formPack.FormActivity
import com.example.present.activities.startPack.gameSignInPack.SignInGameActivity
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityAppModeBinding
import com.example.present.domain.IntentKeys

class AppModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppModeBinding
    private var taskActivity = ""
    private var taskArg = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.extras
        if (data != null) {
            taskActivity = data.getString(IntentKeys.TASK_ACTIVITY, "")
            taskArg = data.getString(IntentKeys.TASK_ARG, "")
        }
        if (taskActivity.isNotEmpty() && taskArg.isNotEmpty()) {
            if (taskActivity == StringProvider.TASK_OPEN_AUTH_GAME) {
                val intent = Intent(this@AppModeActivity, SignInGameActivity::class.java)
                intent.putExtra(IntentKeys.TASK_ARG, taskArg)
                startActivity(intent)
            }
        }
        binding.apply {
            make.setOnClickListener {
                val intent = Intent(this@AppModeActivity, FormActivity::class.java)
                startActivity(intent)
            }
            signIn.setOnClickListener {
                val intent = Intent(this@AppModeActivity, SignInGameActivity::class.java)
                startActivity(intent)
            }
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}