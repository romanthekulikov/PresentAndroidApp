package com.example.present.activities.gamePack.mainPack

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.present.R
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityMainBinding
import kotlin.properties.Delegates

const val HOME_POSITION = 0
const val CHAT_POSITION = 1
const val MAP_POSITION = 2
const val SETTINGS_POSITION = 3

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainVM: MainViewModel
    private var backPressed: Long = System.currentTimeMillis()
    private lateinit var navigationItemsLayoutMap: MutableMap<Int, LinearLayout>
    private lateinit var fragmentMap: MutableMap<Int, Fragment>
    private var selectedNavigationItem by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainVM = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
        navigationItemsLayoutMap = mutableMapOf(
            HOME_POSITION to binding.navigationMainLayout,
            CHAT_POSITION to binding.navigationChatLayout,
            MAP_POSITION to binding.navigationMapLayout,
            SETTINGS_POSITION to binding.navigationSettingsLayout
        )
        fragmentMap = mutableMapOf(
            HOME_POSITION to MainFragment(),
            CHAT_POSITION to MainFragment(),
            MAP_POSITION to MapFragment(),
            SETTINGS_POSITION to MainFragment()
        )
        selectedNavigationItem = mainVM.mutableNavPosition.value!!
        changeNavBar(selectedNavigationItem)
        moveToFragment(fragmentMap[selectedNavigationItem]!!)
        listenersInit()
        addBackDispatcher()
    }

    private fun listenersInit() {
        binding.apply {
            navigationMapLayout.setOnClickListener {
                if (selectedNavigationItem != MAP_POSITION) {
                    changeNavBar(MAP_POSITION)
                    moveToFragment(MapFragment())
                }

            }
            navigationMainLayout.setOnClickListener {
                if (selectedNavigationItem != HOME_POSITION) {
                    changeNavBar(HOME_POSITION)
                    moveToFragment(MainFragment())
                }
            }
            navigationChatLayout.setOnClickListener {
                if (selectedNavigationItem != CHAT_POSITION) {
                    changeNavBar(CHAT_POSITION)
                    //moveToFragment(MainFragment())
                }
            }
            navigationSettingsLayout.setOnClickListener {
                if (selectedNavigationItem != SETTINGS_POSITION) {
                    changeNavBar(SETTINGS_POSITION)
                    //moveToFragment(MainFragment())
                }
            }
        }
    }

    private fun changeNavBar(changeItemTo: Int) {
        updateCurrentNavigationItemWeight(1f, true)
        selectedNavigationItem = changeItemTo
        mainVM.mutableNavPosition.value = changeItemTo
        updateCurrentNavigationItemWeight(2f, false)
    }

    private fun updateCurrentNavigationItemWeight(weight: Float, needHide: Boolean) {
        val previousItemLayout = navigationItemsLayoutMap[selectedNavigationItem]!!
        val parentLayoutParams = previousItemLayout.layoutParams as LinearLayout.LayoutParams
        val previousItem = previousItemLayout.getChildAt(0) as LinearLayout

        parentLayoutParams.weight = weight
        previousItemLayout.layoutParams = parentLayoutParams

        val dp55ToPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            55f,
            resources.displayMetrics
        )
        val itemTextLayout = previousItem.getChildAt(1) as TextView
        val layoutParams = previousItem.layoutParams as LinearLayout.LayoutParams
        if (needHide) {
            previousItem.background = AppCompatResources.getDrawable(
                applicationContext,
                R.drawable.main_circle_border_off
            )
            layoutParams.width = dp55ToPx.toInt()
            itemTextLayout.visibility = View.GONE
        } else {
            previousItem.background = AppCompatResources.getDrawable(
                applicationContext,
                R.drawable.main_circle_border_on
            )
            layoutParams.width = LayoutParams.WRAP_CONTENT
            itemTextLayout.visibility = View.VISIBLE
        }
        previousItem.layoutParams = layoutParams
    }

    private fun moveToFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                fragment
            )
            .commit()
    }

    private fun addBackDispatcher() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressed + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        StringProvider.ON_BACK_PRESSED_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressed = System.currentTimeMillis()
            }
        })
    }
}