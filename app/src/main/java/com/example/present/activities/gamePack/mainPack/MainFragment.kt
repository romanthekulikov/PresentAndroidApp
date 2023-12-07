package com.example.present.activities.gamePack.mainPack

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.present.activities.gamePack.getPresentPack.GetPresentActivity
import com.example.present.data.StringProvider
import com.example.present.databinding.FragmentMainBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys

class MainFragment : Fragment() {

    private lateinit var _binding: FragmentMainBinding
    private lateinit var mainVM: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        mainVM = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]

        initScreen()
        observationInit()
        listenersInit()

        return _binding.root
    }

    private fun listenersInit() {
        _binding.apply {

            getHint.setOnClickListener {
                getAdditionalDialog().show(
                    requireActivity().supportFragmentManager,
                    StringProvider.DIALOG_ADDITIONAL_TAG
                )
            }

            check.setOnClickListener {
                hideKeyboard()
                if (mainVM.checkCode(inputCodeLayout.text.toString())) {
                    getDialog().show(
                        requireActivity().supportFragmentManager,
                        StringProvider.DIALOG_CORRECT_TAG
                    )
                    mainVM.addProgress()
                    inputCodeLayout.setText("")
                } else {
                    getErrorDialog().show(
                        requireActivity().supportFragmentManager,
                        StringProvider.DIALOG_ERROR_TAG
                    )
                }
            }

//            openAllPresent.setOnClickListener {
//                val intent = Intent(this@MainActivity, PresentActivity::class.java)
//                startActivity(intent)
//            }
        }
    }

    private fun initScreen() {
        mainVM.getData()
        _binding.apply {
            hintText.text = mainVM.mutableHint.value
        }
    }

    private fun observationInit() {
        mainVM.mutableHint.observe(requireActivity()) {
            _binding.hintText.text = it
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
            val intent = Intent(requireContext(), GetPresentActivity::class.java)
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
        val imm =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
    }
}