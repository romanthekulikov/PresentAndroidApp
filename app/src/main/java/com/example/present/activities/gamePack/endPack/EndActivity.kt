package com.example.present.activities.gamePack.endPack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.present.R
import com.example.present.activities.gamePack.presentPack.PresentActivity
import com.example.present.activities.profilePack.ProfileActivity
import com.example.present.data.database.AppDatabase
import com.example.present.databinding.ActivityEndBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EndActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEndBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.presents.setOnClickListener {
            val intent = Intent(this, PresentActivity::class.java)
            startActivity(intent)
        }
        loadImage()
    }

    private fun loadImage() {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = AppDatabase.getDB(this@EndActivity).getUserDao()
            val user = userDao.getUser()!!
            CoroutineScope(Dispatchers.Main).launch {
                if (user.icon.isNotEmpty()) {
                    Glide.with(this@EndActivity).load(user.icon).into(binding.iconImage)
                    binding.iconBlock.visibility = View.GONE
                    binding.iconImage.visibility = View.VISIBLE
                }
            }
        }

    }
}