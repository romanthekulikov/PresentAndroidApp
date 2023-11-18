package com.example.present.activities.startPack.formPack

import android.database.sqlite.SQLiteException
import android.os.Bundle
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

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, PointFormFragment(this, pointForm))
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()

        binding.back.setOnClickListener {
            if (currentStage == POINT_STAGE) {
                getDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
            } else {
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
        }

        binding.exit.setOnClickListener {
            getDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
        }
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
        val tt = presentForm!!.image.toString()
        val db = AppDatabase.getDB(context = this)
        val idUser = db.getUserDao().getUser().id
        val formItem = FormItemEntity(
            idStage = 0,
            idUser = idUser,
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

        db.getFormItemDao().saveFormItem(formItem = formItem)
    }
}