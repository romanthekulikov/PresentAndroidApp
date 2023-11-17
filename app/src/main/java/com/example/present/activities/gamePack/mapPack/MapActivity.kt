package com.example.present.activities.gamePack.mapPack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.present.R
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityMapBinding
import com.example.present.dialog.DialogPresent
import com.example.present.domain.IntentKeys
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: MapView
    private var progress = 0

    private val listPoint = listOf(
        Point(56.6512, 47.84233),
        Point(56.64003, 47.86532),
        Point(56.64613, 47.86323),
        Point(56.63695, 47.8869)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progress = intent.getIntExtra(IntentKeys.PROGRESS_KEY, 0)
        binding.hintText.text = StringProvider.hintMap[progress]
        map = binding.mapView

        val presentImage = ImageProvider.fromResource(this, R.drawable.present_img)

        map.map.mapObjects.addPlacemark(
            com.yandex.mapkit.geometry.Point(
                listPoint[progress].latitude,
                listPoint[progress].longitude
            ), presentImage
        )

        map.map.move(
            CameraPosition(
                com.yandex.mapkit.geometry.Point(
                    listPoint[progress].latitude,
                    listPoint[progress].longitude
                ), 14F, 0F, 0F
            )
        )

        listenersInit()
    }

    private fun listenersInit() {
        binding.apply {
            back.setOnClickListener {
                finish()
            }

            getHint.setOnClickListener {
                getAdditionalDialog().show(
                    supportFragmentManager,
                    StringProvider.DIALOG_CORRECT_TAG
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        map.onStart()
    }

    override fun onStop() {
        map.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun getAdditionalDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.additionalHintMap[progress]!!
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.POSITIVE_ADDITIONAL_BUTTON)

        return dialog
    }
}

data class Point(
    var latitude: Double,
    var longitude: Double
)