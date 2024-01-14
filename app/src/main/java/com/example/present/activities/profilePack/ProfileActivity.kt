package com.example.present.activities.profilePack

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.present.activities.startPack.appModePack.AppModeActivity
import com.example.present.activities.startPack.authorizationPack.AuthorizationActivity
import com.example.present.adapters.GamesProgressAdapter
import com.example.present.data.StringProvider
import com.example.present.data.database.AppDatabase
import com.example.present.databinding.ActivityProfileBinding
import com.example.present.dialog.DialogPresent
import com.example.present.remote.ApiProvider
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var iconUri: Uri? = null
    private var isChanged = false
    private val storage = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progress.isActivated = true
        binding.progress.visibility = View.VISIBLE
        binding.iconImage.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }
        binding.icon.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

        showIcon()

        binding.editNameIcon.setOnClickListener {
            binding.name.visibility = View.GONE
            binding.editNameText.visibility = View.VISIBLE
            binding.editNameText.setText(binding.name.text)
            binding.editNameText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editNameText, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.editNameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isChanged = true
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.enter.setOnClickListener {
            if (isChanged) {
                binding.progress.visibility = View.VISIBLE
                val newName = binding.editNameText.text.toString()
                val newIcon = iconUri
                CoroutineScope(Dispatchers.IO).launch {
                    if (newIcon != null) {
                        val dataRef = storage.child("images/${newIcon.lastPathSegment}")
                        val uploadTask = dataRef.putFile(newIcon)
                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            dataRef.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val userDao = AppDatabase.getDB(this@ProfileActivity).getUserDao()
                                    userDao.updateUserInfo(newName, task.result.toString())
                                    val userId = userDao.getUser()!!.id
                                    val userApi = ApiProvider.userApi
                                    userApi.updateUserInfo(userId, newName, task.result.toString()).execute().body()
                                    finish()
                                }
                            }
                        }
                    } else {
                        val userDao = AppDatabase.getDB(this@ProfileActivity).getUserDao()
                        val userId = userDao.getUser()!!.id
                        val userApi = ApiProvider.userApi
                        val y = userApi.updateUserInfo(userId, newName, null).execute().body()
                        finish()
                    }
                }
            } else {
                finish()
            }
        }

        binding.exit.setOnClickListener {
            getWarningDialog().show(supportFragmentManager, "")
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.goToModeText.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDB(this@ProfileActivity)
                db.getChatDao().deleteChatTable()
                db.getGameDao().deleteGameTable()
                db.getPresentDao().deletePresentTable()
                db.getStageDao().deleteStageTable()
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(this@ProfileActivity, AppModeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }

        fillGamesProgress()
    }

    private fun showIcon() {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = AppDatabase.getDB(this@ProfileActivity).getUserDao()
            val userBD = userDao.getUser()!!
            val userApi = ApiProvider.userApi
            val user = userApi.getUserInfo(userBD.email).execute().body()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    if (user!!.icon.isNotEmpty()) {
                        binding.icon.visibility = View.VISIBLE
                        binding.iconImage.visibility = View.GONE
                        Glide.with(this@ProfileActivity).load(user.icon).into(binding.icon)
                        isChanged = false
                    }
                    binding.editNameText.setText(user.name)
                    binding.name.text = user.name
                    binding.email.text = user.email
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileActivity, "Что-то пошло не так(", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getWarningDialog(): DialogPresent {
        val dialog = DialogPresent()
        dialog.setMessage(text = "Выйти из профиля? Необходимо будет заново авторизоваться и войти в игру")
        dialog.setPositiveButtonText(text = StringProvider.EXIT)
        dialog.setPositiveAction {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDB(this@ProfileActivity)
                db.getChatDao().deleteChatTable()
                db.getGameDao().deleteGameTable()
                db.getPresentDao().deletePresentTable()
                db.getStageDao().deleteStageTable()
                db.getUserDao().deleteUserTable()
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(this@ProfileActivity, AuthorizationActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        return dialog
    }

    private fun getBackWarningDialog(): DialogPresent {
        val dialog = DialogPresent()
        dialog.setMessage(text = "Изменения не будут применены, уверены, что хотите выйти?")
        dialog.setPositiveButtonText(text = StringProvider.YES)
        dialog.setPositiveAction {
            finish()
        }

        return dialog
    }

    private fun fillGamesProgress() {
        CoroutineScope(Dispatchers.IO).launch {
            val gameApi = ApiProvider.gameApi
            val db = AppDatabase.getDB(this@ProfileActivity)
            val user = db.getUserDao().getUser()
            val progressList = gameApi.getGamesProgress(user!!.id).execute().body()
            CoroutineScope(Dispatchers.Main).launch {
                if (progressList != null) {
                    binding.gameRecycler.layoutManager = LinearLayoutManager(this@ProfileActivity)
                    binding.gameRecycler.adapter = GamesProgressAdapter(progressList, user.id)
                }
                binding.progress.visibility = View.GONE
            }

        }
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                if (imgUri != null) {
                    try {
                        binding.icon.setImageURI(imgUri)
                        binding.iconImage.visibility = View.GONE
                        binding.icon.visibility = View.VISIBLE
//                        val source = ImageDecoder.createSource(
//                            this.contentResolver,
//                            imgUri
//                        )
//                        var bitmap = ImageDecoder.decodeBitmap(source)
//                        bitmap =
//                            bitmap?.let { it1 -> Bitmap.createScaledBitmap(it1, 1200, 600, true) }
//
//                        _binding.galleryImage.setImageBitmap(bitmap)
//                        binding.iconImage.setImageBitmap()
                        isChanged = true
                    } catch (_: Exception) {}

                    iconUri = imgUri
                }
            }
        }
}