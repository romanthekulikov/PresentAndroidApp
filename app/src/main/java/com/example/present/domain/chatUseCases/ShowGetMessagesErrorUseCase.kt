package com.example.present.domain.chatUseCases

import android.app.Activity
import android.widget.Toast
import com.google.firebase.database.DatabaseError

class ShowGetMessagesErrorUseCase(private val activity: Activity) {
    fun execute(error: DatabaseError) {
        Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
    }
}