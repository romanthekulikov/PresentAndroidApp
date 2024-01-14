package com.example.present.activities.gamePack.mainPack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.present.R
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
    private lateinit var map: MapView

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
        listenersInit()
        observationInit()

        return _binding.root
    }

    private fun observationInit() {
        mainVM.mutableLong.observe(requireActivity()) {
            mapInit(mainVM.mutableLat.value!!, it)
        }
    }

    private fun initData() {
        mainVM.getData()
        _binding.hintText.text = mainVM.mutableHint.value
    }

    private fun mapInit(lat: Double, long: Double) {
        try {
            map = _binding.mapView
            val presentImage = ImageProvider.fromResource(requireContext(), R.drawable.present_img)

            map.map.mapObjects.addPlacemark(
                com.yandex.mapkit.geometry.Point(
                    lat,
                    long
                ), presentImage
            )

            map.map.move(
                CameraPosition(
                    com.yandex.mapkit.geometry.Point(
                        lat,
                        long
                    ), 14F, 0F, 0F
                )
            )
        } catch (_: Exception) {}
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
        val message = mainVM.mutableAdditionalHint.value
        dialog.setMessage(text = message!!)
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