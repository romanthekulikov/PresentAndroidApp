package com.example.present.activities.startPack.gameSignInPack

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.present.R
import com.example.present.activities.gamePack.mainPack.MainActivity
import com.example.present.activities.profilePack.ProfileActivity
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.ChatEntity
import com.example.present.data.database.entities.GameEntity
import com.example.present.data.database.entities.PresentEntity
import com.example.present.data.database.entities.StageEntity
import com.example.present.databinding.ActivitySignInGameBinding
import com.example.present.domain.IntentKeys
import com.example.present.remote.ApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInGameBinding
    private lateinit var layoutAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = intent.extras?.getString(IntentKeys.TASK_ARG, "")
        binding.password.setText(key)
        listenersInit()
        animationInit()
        addBackPressed()
    }

    private fun animationInit() {
        layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.appear)
        binding.mainLayout.startAnimation(layoutAnimation)
        binding.mainLayout.translationY = -400f
        ViewCompat.animate(binding.mainLayout)
            .translationYBy(400f)
            .setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator()).setStartDelay(150)
            .withEndAction {
                ViewCompat.animate(binding.backLayout)
                    .setDuration(500)
                    .alphaBy(1f)
                    .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
                ViewCompat.animate(binding.seekBar)
                    .setDuration(600)
                    .alphaBy(1f)
                    .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 150
            }
    }

    private fun listenersInit() {
        binding.back.setOnClickListener {
            finish()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (progress == 100) {
                        try {
                            val gameSet = getGameSetIfExist()
                            if (gameSet != null) {
                                try {
                                    val db = AppDatabase.getDB(applicationContext)
                                    val stages = gameSet.stages
                                    val presents = gameSet.presents
                                    val responseGame = gameSet.game
                                    val stageDao = db.getStageDao()
                                    val presentDao = db.getPresentDao()
                                    for (i in stages.indices) {
                                        val stage = StageEntity(
                                            stages[i].id_stage,
                                            stages[i].id_game,
                                            stages[i].id_present,
                                            stages[i].text_stage,
                                            stages[i].hint_text,
                                            stages[i].long,
                                            stages[i].lat,
                                            stages[i].is_done
                                        )
                                        stageDao.saveStage(stage)
                                        val present = PresentEntity(
                                            stages[i].id_present,
                                            stages[i].id_stage,
                                            presents[i].present_text,
                                            presents[i].present_img,
                                            presents[i].redirect_link
                                        )
                                        presentDao.savePresent(present)
                                    }
                                    val game = GameEntity(
                                        responseGame.id_game,
                                        responseGame.id_admin,
                                        responseGame.id_user,
                                        responseGame.start_date,
                                        responseGame.chat_id
                                    )
                                    db.getGameDao().saveGame(game)
                                    val chatApi = ApiProvider.chatApi
                                    val chatSettings = chatApi.getChatSettings(responseGame.chat_id).execute().body()
                                    if (chatSettings != null) {
                                        db.getChatDao().insertChat(
                                            ChatEntity(
                                                chatSettings.id,
                                                chatSettings.bgColor,
                                                chatSettings.bgImage,
                                                chatSettings.textSize
                                            )
                                        )
                                    }
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val intent = Intent(this@SignInGameActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                } catch (e: Exception) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        Toast.makeText(this@SignInGameActivity, e.message, Toast.LENGTH_LONG).show()
                                    }
                                }

                            }
                        } catch (e: Exception) {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(
                                    applicationContext,
                                    StringProvider.ERROR_SEND_MESSAGE,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getGameSetIfExist(): EnterResponse? {
        val gameApi = ApiProvider.gameApi
        val db = AppDatabase.getDB(applicationContext)
        val user = db.getUserDao().getUser()
        val gameSet = gameApi.enterTheGame(user!!.id, binding.password.text.toString()).execute().body()
        if (gameSet == null) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@SignInGameActivity, "Проблемы сервера. Попробуйте позже", Toast.LENGTH_LONG).show()
            }
        } else if (gameSet.error != null) {
            binding.password.error = "Не правильный пароль"
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@SignInGameActivity, gameSet.message, Toast.LENGTH_LONG).show()
            }
            return null
        }

        return gameSet
    }

    private fun addBackPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(backPressedCallback)
    }

    data class EnterResponse(
        val game: EnterGameObject,
        val stages: List<EnterStageObject>,
        val presents: List<EnterPresentObject>,
        val error: Int?,
        val message: String?
    )

    data class EnterGameObject(
        val id_game: Int,
        val id_admin: Int,
        val id_user: Int,
        val start_date: String,
        val chat_id: Int
    )

    data class EnterStageObject(
        val id_stage: Int,
        val id_present: Int,
        val id_game: Int,
        val text_stage: String,
        val hint_text: String,
        val long: Double,
        val lat: Double,
        val is_done: Boolean
    )

    data class EnterPresentObject(
        val present_text: String,
        val present_img: String,
        val redirect_link: String
    )
}