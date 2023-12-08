package com.example.present.activities.startPack.formPack.formStages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.present.data.models.Point
import com.example.present.data.StringProvider
import com.example.present.data.models.PointFormModel
import com.example.present.databinding.FrPointFormBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition

class PointFormFragment(
    private val onClick: PointOnNextClick,
    private val pointForm: PointFormModel?
) : Fragment() {

    private lateinit var _binding: FrPointFormBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrPointFormBinding.inflate(layoutInflater)
        _binding.mapView.map.isScrollGesturesEnabled = false
        fillField()
        listenersInit()
        return _binding.root
    }

    private fun fillField() {
        if (pointForm != null) {
            _binding.apply {
                lat.setText(pointForm.point.latitude.toString())
                lng.setText(pointForm.point.longitude.toString())
                text.setText(pointForm.text)
                hint.setText(pointForm.hint)
            }
            moveMap()
        }
    }

    private fun listenersInit() {
        _binding.next.setOnClickListener {
            sendPointToActivity()
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                moveMap()
            }
        }
        _binding.lat.addTextChangedListener(textWatcher)
        _binding.lng.addTextChangedListener(textWatcher)
    }

    private fun sendPointToActivity() {
        val pointForm: PointFormModel?
        val lat = _binding.lat.text
        val lng = _binding.lng.text
        val text = _binding.text.text.toString()
        val hint = _binding.hint.text.toString()
        var pointHasError = false
        if (lat.isNullOrEmpty()) {
            pointHasError = true
            _binding.lat.error = StringProvider.ERROR_LONGITUDE_POINT
        }
        if (lng.isNullOrEmpty()) {
            pointHasError = true
            _binding.lng.error = StringProvider.ERROR_LATITUDE_POINT
        }
        if (text.isEmpty()) {
            pointHasError = true
            _binding.text.error = StringProvider.ERROR_TEXT_POINT
        }
        if (hint.isEmpty()) {
            pointHasError = true
            _binding.hint.error = StringProvider.ERROR_HINT_POINT
        }

        if (!pointHasError) {

            pointForm = PointFormModel(
                point = Point(lat.toString().toDouble(), lng.toString().toDouble()),
                text = text, hint = hint
            )
            onClick.onNextClickPoint(pointForm)
        }
    }

    private fun moveMap() {
        try {
            _binding.mapView.map.move(
                CameraPosition(
                    com.yandex.mapkit.geometry.Point(
                        _binding.lat.text.toString().toDouble(),
                        _binding.lng.text.toString().toDouble()
                    ), 14F, 0F, 0F
                )
            )
        } catch (_: NumberFormatException) {}

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        _binding.mapView.onStart()
    }

    override fun onStop() {
        _binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    interface PointOnNextClick {
        fun onNextClickPoint(pointForm: PointFormModel)
    }
}

