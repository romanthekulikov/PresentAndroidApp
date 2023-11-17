package com.example.present.activities.startPack.welcomePack.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.present.databinding.FrTutorial2Binding

class TutorialSecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrTutorial2Binding.inflate(layoutInflater).root
    }
}