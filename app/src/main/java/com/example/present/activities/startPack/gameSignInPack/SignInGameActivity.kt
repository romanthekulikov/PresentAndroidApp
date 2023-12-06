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
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.GameEntity
import com.example.present.data.database.entities.PresentEntity
import com.example.present.data.database.entities.StageEntity
import com.example.present.databinding.ActivitySignInGameBinding
import com.example.present.domain.IntentKeys
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
                if (progress == 100) {
                    if (passwordIsCorrect()) {
                        //TODO: Тут будет обмен с ВС и получение данных по
                        // игре(Если приходящий id_admin == id_user, то показываем mod admin)
                        //TODO: Убрать заглушку(запись в БД пришедших данных):
                        CoroutineScope(Dispatchers.IO).launch {
                            val game = GameEntity(0, 0, 1, System.currentTimeMillis())
                            val present1 = PresentEntity(
                                0,
                                0,
                                "Поздравительный текст",
                                "",
                                StringProvider.OZON_REDIRECT_URL
                            )
                            val present2 = PresentEntity(
                                1,
                                1,
                                "Еще один поздравительный текст",
                                "",
                                "http://"
                            )
                            val stage1 = StageEntity(
                                0,
                                0,
                                "Текст точки",
                                "Подсказка для точки",
                                47.84233,
                                56.6512,
                                false
                            )
                            val stage2 = StageEntity(
                                1,
                                0,
                                "Еще один текст точки",
                                "Еще одна подсказка для точки",
                                47.86532,
                                56.64003,
                                false
                            )
                            try {
                                val db = AppDatabase.getDB(applicationContext)
                                db.getGameDao().saveGame(game)
                                db.getPresentDao().savePresent(present1)
                                db.getPresentDao().savePresent(present2)
                                db.getStageDao().saveStage(stage1)
                                db.getStageDao().saveStage(stage2)

                                CoroutineScope(Dispatchers.Main).launch {
                                    val intent = Intent(this@SignInGameActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
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
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun passwordIsCorrect(): Boolean {
        var isCorrect = true
        //TODO: Пока без проверок на ВС, длина тоже будет варийроваться
        if (binding.password.text.toString().length < 7) {
            binding.password.error = "Не правильный пароль"
            isCorrect = false
        }

        return isCorrect
    }

    private fun addBackPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(backPressedCallback)
    }
}