package com.example.poidem_gulyat.ui.homeActivity.home

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.databinding.FragmentHomeBinding
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var context: MainActivity? = null

    private val binding: FragmentHomeBinding by viewBinding()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        // человечек на карте
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), binding.map);
        binding.map.overlays.add(locationOverlay)
        // повороты
        val rotationGestureOverlay =  RotationGestureOverlay(context,  binding.map);
        rotationGestureOverlay.isEnabled
        binding.map.setMultiTouchControls(true);
        binding.map.overlays.add(rotationGestureOverlay);
        // инициализация карты типа координата, тут нужен будет сервис координат. +анимация загрузки прикольная
        val mapController =  binding.map.controller
        mapController.setZoom(9.5)
        val startPoint = GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
    }

    override fun onResume() {
        super.onResume();
        binding.map.onResume();
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause();
        binding.map.onPause();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}