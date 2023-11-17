package com.example.present.data.models

import android.net.Uri

data class PresentFormModel(
    var congratulationText: String,
    var link: String,
    var key: String,
    var image: Uri
)
