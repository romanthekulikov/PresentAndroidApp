package com.example.present.activities.startPack.formPack

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.present.activities.startPack.formPack.formStages.PointFormFragment
import com.example.present.activities.startPack.formPack.formStages.PresentFormFragment
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.data.database.entities.PresentEntity
import com.example.present.data.database.entities.StageEntity
import com.example.present.databinding.ActivityAddPresentBinding
import com.example.present.dialog.DialogPresent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val POINT_STAGE = 0
const val PRESENT_STAGE = 1

class AddPresentActivity : FragmentActivity() {
    private lateinit var binding: ActivityAddPresentBinding
    private var currentStage = POINT_STAGE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPresentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, PointFormFragment.newInstance())
            .commit()

        binding.next.setOnClickListener {
            if (currentStage == POINT_STAGE) {
                val point = PointFormFragment.pointForm
                if (point.point.latitude != 0.0 && point.point.longitude != 0.0 && point.text != "" && point.hint != "") {
                    currentStage = PRESENT_STAGE
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragmentContainer.id, PresentFormFragment.newInstance())
                        .commit()
                    binding.next.text = StringProvider.DONE
                } else {
                    getErrorDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
                }
            } else {
                //TODO: Есть большие проблемы с валидацией данных в PointForm
                val present = PresentFormFragment.present
                if (present.congratulationText != "" && present.link != "" && present.key != "") {
                    binding.progress.visibility = View.VISIBLE
                    CoroutineScope(Dispatchers.IO).launch {
                        val db = AppDatabase.getDB(applicationContext)
                        val pointForm = PointFormFragment.pointForm
                        val stage = StageEntity(
                            0,
                            0,
                            pointForm.text,
                            pointForm.hint,
                            pointForm.point.longitude,
                            pointForm.point.latitude,
                            false
                        )
                        db.getStageDao().saveStage(stage)
                        val dbStage = db.getStageDao().getLastStage()
                        val dbPresent = PresentEntity(
                            dbStage.id,
                            dbStage.id,
                            present.congratulationText,
                            present.image.toString(),
                            present.link
                        )

                        db.getPresentDao().savePresent(dbPresent)

                        CoroutineScope(Dispatchers.Main).launch {
                            binding.progress.visibility = View.GONE
                            finish()
                        }
                    }
                } else {
                    getErrorDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
                }
            }
        }

        binding.back.setOnClickListener {
            if (currentStage == POINT_STAGE) {
                getDialog().show(supportFragmentManager, StringProvider.DIALOG_ERROR_TAG)
            } else {
                supportFragmentManager
                    .beginTransaction()
                    .replace(binding.fragmentContainer.id, PointFormFragment.newInstance())
                    .commit()
                binding.next.text = StringProvider.NEXT
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

    private fun getErrorDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.POINT_FORM_ERROR
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.CONTINUE)
        return dialog
    }
}