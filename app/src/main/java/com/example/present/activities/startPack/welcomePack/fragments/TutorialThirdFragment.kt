package com.example.present.activities.startPack.welcomePack.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.present.databinding.FrTutorial3Binding

class TutorialThirdFragment : Fragment() {
    private lateinit var _binding: FrTutorial3Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrTutorial3Binding.inflate(layoutInflater)
        return _binding.root
    }
}