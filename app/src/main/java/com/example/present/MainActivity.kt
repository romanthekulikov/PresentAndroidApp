package com.example.present

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityMainBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys
import com.example.present.getPresentPack.GetPresentActivity
import com.example.present.mapPack.MapActivity
import com.example.present.presentPack.PresentActivity
import com.example.present.rulesPack.RulesActivity
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainVM: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(StringProvider.API)  //Инициализация API для Яндекс карт
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainVM = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]

        initScreen()
        observationInit()
        listenersInit()
    }

    private fun initScreen() {
        mainVM.getData()
        binding.apply {
            hintText.text = mainVM.mutableHint.value
        }
    }

    private fun observationInit() {
        mainVM.mutableHint.observe(this) {
            binding.hintText.text = it
        }
    }

    private fun listenersInit() {
        binding.apply {
            rules.setOnClickListener {
                val intent = Intent(this@MainActivity, RulesActivity::class.java)
                startActivity(intent)
            }

            map.setOnClickListener {
                val progress = mainVM.mutableProgress.value
                val intent = Intent(this@MainActivity, MapActivity::class.java)
                intent.putExtra(IntentKeys.PROGRESS_KEY, progress)
                startActivity(intent)
            }

            getHint.setOnClickListener {
                getAdditionalDialog().show(
                    supportFragmentManager,
                    StringProvider.DIALOG_ADDITIONAL_TAG
                )
            }

            check.setOnClickListener {
                hideKeyboard()
                if (mainVM.checkCode(inputCodeLayout.text.toString())) {
                    getDialog().show(supportFragmentManager, StringProvider.DIALOG_CORRECT_TAG)
                    mainVM.addProgress()
                    inputCodeLayout.setText("")
                } else {
                    getErrorDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
                }
            }

            openAllPresent.setOnClickListener {
                val intent = Intent(this@MainActivity, PresentActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getDialog(): DialogPresent {
        val progress = mainVM.mutableProgress.value
        val dialog = DialogPresent()
        val message = StringProvider.dialogCongratulationTitleMap[progress]!!
        dialog.setMessage(text = message)
        dialog.setNegativeButtonText(text = StringProvider.NEGATIVE_DIALOG_BUTTON)
        dialog.setPositiveButtonText(text = StringProvider.POSITIVE_DIALOG_BUTTON)
        dialog.setPositiveAction {
            val intent = Intent(this, GetPresentActivity::class.java)
            intent.putExtra(IntentKeys.PROGRESS_KEY, progress)
            startActivity(intent)
        }

        return dialog
    }

    private fun getAdditionalDialog(): DialogPresent {
        val progress = mainVM.mutableProgress.value
        val dialog = DialogPresent()
        val message = StringProvider.additionalHintMap[progress]!!
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.POSITIVE_ADDITIONAL_BUTTON)

        return dialog
    }

    private fun getErrorDialog(): DialogPresent {
        val dialog = DialogPresent()
        dialog.setMessage(text = StringProvider.DIALOG_ERROR_MESSAGE)
        dialog.setPositiveButtonText(text = StringProvider.DIALOG_UNDERSTAND_BUTTON)

        return dialog
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}