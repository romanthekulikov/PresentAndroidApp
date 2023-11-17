package com.example.present.activities.startPack.welcomePack

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.present.activities.startPack.appModePack.AppModeActivity
import com.example.present.adapters.TutorialAdapter
import com.example.present.data.Pref
import com.example.present.databinding.ActivityWelcomeBinding
import kotlin.math.abs

class WelcomeActivity : FragmentActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerInit()
        listenersInit()
        addBackPressed()
    }

    private fun listenersInit() {
        binding.go.setOnClickListener {
            Pref(context = applicationContext).saveFirstOpening(false)
            val intent = Intent(this@WelcomeActivity, AppModeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun pagerInit() {
        viewPager = binding.viewPager
        viewPager.adapter = TutorialAdapter(this)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    binding.go.visibility = View.VISIBLE
                } else {
                    binding.go.visibility = View.GONE
                }
                super.onPageSelected(position)
            }
        })

        viewPager.setPageTransformer { page, position ->
            val height = page.height.toFloat()
            val width = page.width.toFloat()
            val scale: Float = if (position > 0) 1f else abs(1f + position).coerceAtMost(1f)

            page.scaleX = scale
            page.scaleY = scale
            page.pivotX = width * 0.5f
            page.pivotY = height * 0.5f
            page.translationX = if (position > 0) width * position else -width * position * 0.25f
        }
    }

    private fun addBackPressed() {
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewPager.currentItem == 0) {
                    finish()
                } else {
                    viewPager.currentItem = viewPager.currentItem - 1
                }
            }

        }
        this.onBackPressedDispatcher.addCallback(backCallback)
    }
}