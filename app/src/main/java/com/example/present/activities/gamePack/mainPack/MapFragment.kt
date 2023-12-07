package com.example.present.activities.gamePack.mainPack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.present.R
import com.example.present.activities.gamePack.mapPack.Point
import com.example.present.data.StringProvider
import com.example.present.databinding.FragmentMapBinding
import com.example.present.dialog.DialogPresent
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class MapFragment : Fragment() {
    private lateinit var _binding: FragmentMapBinding
    private lateinit var mainVM: MainViewModel
    private var progress = 0
    private lateinit var map: MapView
    private val listPoint = listOf(
        Point(56.6512, 47.84233),
        Point(56.64003, 47.86532),
        Point(56.64613, 47.86323),
        Point(56.63695, 47.8869)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        mainVM = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]

        initData()
        mapInit()
        listenersInit()

        return _binding.root
    }

    private fun initData() {
        mainVM.getData()
        progress = mainVM.mutableProgress.value!!
        _binding.hintText.text = mainVM.mutableHint.value
    }

    private fun mapInit() {
        map = _binding.mapView
        val presentImage = ImageProvider.fromResource(requireContext(), R.drawable.present_img)

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
    }

    private fun listenersInit() {
        _binding.apply {

            getHint.setOnClickListener {
                getAdditionalDialog().show(
                    requireActivity().supportFragmentManager,
                    StringProvider.DIALOG_CORRECT_TAG
                )
            }
        }
    }

    private fun getAdditionalDialog(): DialogPresent {
        val dialog = DialogPresent()
        val message = StringProvider.additionalHintMap[progress]!!
        dialog.setMessage(text = message)
        dialog.setPositiveButtonText(text = StringProvider.POSITIVE_ADDITIONAL_BUTTON)

        return dialog
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
}