package com.example.present.activities.gamePack.mainPack

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.present.R
import com.example.present.activities.gamePack.chatPack.ChatActivity
import com.example.present.activities.gamePack.endPack.EndActivity
import com.example.present.activities.gamePack.presentPack.PresentActivity
import com.example.present.activities.profilePack.ProfileActivity
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.databinding.ActivityMainBinding
import com.example.present.domain.IntentKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val taskActivity = intent.extras?.getString(IntentKeys.TASK_ACTIVITY, null)
        val taskArg =  intent.extras?.getString(IntentKeys.TASK_ARG, null)
        if (taskActivity == "main") {
            mainVM.taskArg = taskArg
        }
        navigationItemsLayoutMap = mutableMapOf(
            HOME_POSITION to binding.navigationMainLayout,
            CHAT_POSITION to binding.navigationChatLayout,
            MAP_POSITION to binding.navigationMapLayout,
            SETTINGS_POSITION to binding.navigationPresentsLayout
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        mainVM.mutableHint.observe(this) {
            if (it == "") {
                val intent = Intent(this, EndActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
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
                CoroutineScope(Dispatchers.IO).launch {
                    val chatDao = AppDatabase.getDB(this@MainActivity).getChatDao()
                    val chatId = chatDao.getChat().idChat
                    val userDao = AppDatabase.getDB(this@MainActivity).getUserDao()
                    val userId = userDao.getUser()!!.id
                    CoroutineScope(Dispatchers.Main).launch {
                        val intent = Intent(this@MainActivity, ChatActivity::class.java)
                        intent.putExtra("chatId", chatId)
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                    }
                }

            }
            navigationPresentsLayout.setOnClickListener {
                if (selectedNavigationItem != SETTINGS_POSITION) {
                    val intent = Intent(this@MainActivity, PresentActivity::class.java)
                    startActivity(intent)
                }
            }
            profile.setOnClickListener {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
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