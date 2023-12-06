package com.example.present.activities.startPack.formPack

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.example.present.activities.startPack.formPack.formStages.PointFormFragment
import com.example.present.activities.startPack.formPack.formStages.PresentFormFragment
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.FormItemEntity
import com.example.present.data.models.PointFormModel
import com.example.present.data.models.PresentFormModel
import com.example.present.databinding.ActivityAddPresentBinding
import com.example.present.dialog.DialogPresent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val POINT_STAGE = 0
const val PRESENT_STAGE = 1

class AddPresentActivity : FragmentActivity(), PointFormFragment.PointOnNextClick,
    PresentFormFragment.PresentOnNextClick {
    private lateinit var binding: ActivityAddPresentBinding
    private var currentStage = POINT_STAGE
    private var pointForm: PointFormModel? = null
    private var presentForm: PresentFormModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPresentBinding.inflate(layoutInflater)

        setContentView(binding.root)
        addBackPressed()
        listenersInit()
        moveToFirstStage()
    }

    private fun listenersInit() {
        binding.back.setOnClickListener {
            if (currentStage == POINT_STAGE) {
                getDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
            } else {
                moveToFirstStage()
            }
        }

        binding.exit.setOnClickListener {
            getDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
        }
    }

    private fun moveToFirstStage() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                PointFormFragment(onClick = this, pointForm = pointForm)
            )
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
        currentStage = POINT_STAGE
    }

    private fun moveToSecondStage() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                PresentFormFragment(onClick = this, presentForm = presentForm)
            )
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
        currentStage = PRESENT_STAGE
    }

    private fun getDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.EXIT_FORM_REDACTOR
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.EXIT)
        dialog.setNegativeButtonText(text = StringProvider.CONTINUE)
        dialog.setPositiveAction {
            finish()
        }

        return dialog
    }

    override fun onNextClickPoint(pointForm: PointFormModel) {
        this.pointForm = pointForm
        moveToSecondStage()
    }

    override fun onCompleteClickPresent(presentForm: PresentFormModel) {
        this.presentForm = presentForm
        getWarningDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
    }

    override fun onBack(presentForm: PresentFormModel) {
        this.presentForm = presentForm
    }

    private fun getWarningDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.COMPLETE_FORM_REDACTOR
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.YES)
        dialog.setNegativeButtonText(text = StringProvider.CONTINUE)
        dialog.setPositiveAction {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    saveFormItem()
                } catch (ex: SQLiteException) {
                    getErrorDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
                }
            }
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
            finish()
        }

        return dialog
    }

    private fun getErrorDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.COMPLETE_FORM_REDACTOR
        dialog.setMessage(text = message)
        dialog.setNegativeButtonText(text = StringProvider.CONTINUE)

        return dialog
    }

    private fun saveFormItem() {
        val db = AppDatabase.getDB(context = this)
        val idUser = db.getUserDao().getUser()?.id
        val formItem = idUser?.let {
            FormItemEntity(
                idStage = 0,
                idUser = it,
                textStage = pointForm!!.text,
                hintText = pointForm!!.hint,
                longitude = pointForm!!.point.longitude,
                latitude = pointForm!!.point.latitude,
                congratulation = presentForm!!.congratulationText,
                presentImg = presentForm!!.image.toString(),
                key = presentForm!!.key,
                keyOpen = presentForm!!.keyOpen,
                link = presentForm!!.link
            )
        }

        if (formItem != null) {
            db.getFormItemDao().saveFormItem(formItem = formItem)
        }
    }

    private fun addBackPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentStage == POINT_STAGE) {
                    getDialog().show(supportFragmentManager, StringProvider.DIALOG_GO_TAG)
                } else {
                    moveToFirstStage()
                }
            }
        }
        onBackPressedDispatcher.addCallback(backPressedCallback)
    }
}