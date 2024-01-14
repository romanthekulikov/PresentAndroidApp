package com.example.present.activities.gamePack.mainPack

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.present.activities.gamePack.endPack.EndActivity
import com.example.present.activities.gamePack.getPresentPack.GetPresentActivity
import com.example.present.data.StringProvider
import com.example.present.databinding.FragmentMainBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys
import com.example.present.remote.ApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                CoroutineScope(Dispatchers.IO).launch {
                    val gameApi = ApiProvider.gameApi
                    val response =
                        gameApi.checkStageKey(mainVM.mutableStageId.value!!, inputCodeLayout.text.toString()).execute().body()
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            if (response!!.error == 200) {
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
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Что-то пошло не так(", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }

    private fun initScreen() {
        mainVM.getData()
        _binding.apply {
            hintText.text = mainVM.mutableHint.value
            _binding.inputCodeLayout.setText(mainVM.taskArg)
        }
    }

    private fun observationInit() {
        mainVM.mutableHint.observe(requireActivity()) {
            _binding.hintText.text = it
            if (it == "") {
                val intent = Intent(requireContext(), EndActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getDialog(): DialogPresent {
        val idPresent = mainVM.mutablePresentId.value
        val dialog = DialogPresent()
        val message = StringProvider.congratulationDialogText
        dialog.setMessage(text = message)
        dialog.setNegativeButtonText(text = StringProvider.NEGATIVE_DIALOG_BUTTON)
        dialog.setPositiveButtonText(text = StringProvider.POSITIVE_DIALOG_BUTTON)
        dialog.setPositiveAction {
            val intent = Intent(requireContext(), GetPresentActivity::class.java)
            intent.putExtra(IntentKeys.PROGRESS_KEY, idPresent)
            startActivity(intent)
        }

        return dialog
    }

    private fun getAdditionalDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = mainVM.mutableAdditionalHint.value!!
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