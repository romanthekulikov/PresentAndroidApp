package com.example.present.activities.startPack.formPack.formStages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.present.activities.gamePack.mapPack.Point
import com.example.present.data.models.PointFormModel
import com.example.present.databinding.FragmentPointFormBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition

class PointFormFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = PointFormFragment()
        var pointForm = PointFormModel(Point(0.0, 0.0), "", "")
    }

    private lateinit var _binding: FragmentPointFormBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointFormBinding.inflate(layoutInflater)
        _binding.mapView.map.isScrollGesturesEnabled = false
        if (pointForm.point.latitude != 0.0 && pointForm.point.longitude != 0.0) {
            _binding.lat.setText(pointForm.point.latitude.toString())
            _binding.lng.setText(pointForm.point.longitude.toString())
            moveMap()
        }
        listenersInit()

        return _binding.root
    }

    private fun listenersInit() {
        _binding.lat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    pointForm.point.latitude = _binding.lat.text.toString().toDouble()
                } catch (_: NumberFormatException) {}
                moveMap()
            }

        })

        _binding.lng.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                moveMap()
                try {
                    pointForm.point.longitude = _binding.lng.text.toString().toDouble()
                } catch (_: NumberFormatException) {}
            }

        })

        _binding.text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                moveMap()
                pointForm.text = _binding.text.toString()
            }

        })

        _binding.text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                pointForm.text = _binding.text.toString()
            }
        })

        _binding.hint.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                pointForm.hint = _binding.hint.toString()
            }
        })
    }

    private fun moveMap() {
        _binding.mapView.map.move(
            CameraPosition(
                com.yandex.mapkit.geometry.Point(
                    pointForm.point.latitude,
                    pointForm.point.longitude
                ), 14F, 0F, 0F
            )
        )
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


}