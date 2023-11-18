package com.example.present.data.models

import android.graphics.Bitmap
import android.net.Uri

data class PresentFormModel(
    var congratulationText: String,
    var link: String,
    var key: String,
    var keyOpen: String,
    var image: Uri?,
    var qr: Bitmap?
)
