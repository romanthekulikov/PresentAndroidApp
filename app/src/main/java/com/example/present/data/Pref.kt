package com.example.present.data

import android.content.Context

const val NAME_PREF = "MainPreferences"
const val TAG_PROGRESS = "count"
const val TAG_SAW_ADD_PRESENT_TUTORIAL_DIALOG = "present_tutorial_dialog"
const val TAG_BE_FIRST = "be_first"

class Pref(context: Context) {
    private val preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE)

    fun saveProgress(progress: Int) {
        val editor = preferences.edit()
        editor.putInt(TAG_PROGRESS, progress)
        editor.apply()
    }

    fun getProgress(): Int {
        return preferences.getInt(TAG_PROGRESS, 0)
    }

    fun saveFirstOpening(beFirst: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(TAG_BE_FIRST, beFirst)
        editor.apply()
    }

    fun getFirstOpening(): Boolean {
        return preferences.getBoolean(TAG_BE_FIRST, true)
    }

    fun getSawPresentTutorial(): Boolean {
        return preferences.getBoolean(TAG_SAW_ADD_PRESENT_TUTORIAL_DIALOG, true)
    }

    fun saveSawPresentTutorial(saw: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(TAG_SAW_ADD_PRESENT_TUTORIAL_DIALOG, saw)
        editor.apply()
    }
}