package com.example.present.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.present.activities.startPack.welcomePack.fragments.TutorialFirstFragment
import com.example.present.activities.startPack.welcomePack.fragments.TutorialSecondFragment
import com.example.present.activities.startPack.welcomePack.fragments.TutorialThirdFragment

class TutorialAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment = TutorialFirstFragment()
        when (position) {
            0 -> fragment = TutorialFirstFragment()
            1 -> fragment = TutorialSecondFragment()
            2 -> fragment = TutorialThirdFragment()
        }

        return fragment
    }
}