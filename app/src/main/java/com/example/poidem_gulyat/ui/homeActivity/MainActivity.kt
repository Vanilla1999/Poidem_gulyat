package com.example.poidem_gulyat.ui.homeActivity

import android.content.ComponentName
import android.content.ServiceConnection
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.databinding.ActivityMainBinding
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.poidem_gulyat.App.Companion.appComponentMain
import com.example.poidem_gulyat.customView.geo.CustomMe
import com.example.poidem_gulyat.data.ErrorApp
import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.ResponseHome
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.dto.UserPoint
import com.example.poidem_gulyat.di.mainActivtiy.DaggerMainActvitityComponent
import com.example.poidem_gulyat.di.mainActivtiy.MainActvitityComponent
import com.example.poidem_gulyat.services.LocationService
import com.example.poidem_gulyat.ui.homeActivity.home.FactoryHomeView
import com.example.poidem_gulyat.ui.homeActivity.home.HomeViewModel
import com.example.poidem_gulyat.utils.BaseActivity
import com.example.poidem_gulyat.utils.tryCast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class MainActivity : BaseActivity(), ServiceConnection, CoroutineScope {
    lateinit var activityComponent: MainActvitityComponent

    @Inject
    lateinit var factory: FactoryMainView
    private val viewModelMain by viewModels<MainViewModel> { factory }
    private lateinit var binding: ActivityMainBinding
    private val resourdisplayMetrics: DisplayMetrics by lazy { resources.displayMetrics }
    lateinit var navView: BottomNavigationView
    private var locationService: LocationService? = null
    private var locationUpdatesJob: Job? = null
    private var firstOpen = true
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    private lateinit var mapController: IMapController
    private var context: MainActivity? = null
    override fun onAfterRequestPermission() {
        //  TODO("Not yet implemented")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("onTouchEvent", "MainActivity")
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerMainActvitityComponent.factory().create(appComponentMain)
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        context = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        setWindowTransparency { _, navigationBarSize ->
            navView.setPadding(0, 0, 0, navigationBarSize)
        }
        initMap()
        initFlowDatabase()
        intiFlowError()
    }

    private fun initMap() {
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        // человечек на карте
        val locationOverlay = CustomMe(binding.map);
        binding.map.overlays.add(locationOverlay)
        // повороты
        val rotationGestureOverlay = RotationGestureOverlay(this, binding.map);
        rotationGestureOverlay.isEnabled
        binding.map.setMultiTouchControls(true);
        binding.map.overlays.add(rotationGestureOverlay);
    }

    private fun initFlowMylocation() {
        locationUpdatesJob = lifecycleScope.launch {
            viewModelMain.locationFlow!!.collect {
                when (it) {
                    is ResponseSplash.Success -> {
                        val location = (it.value as Location)
                        if (firstOpen) {
                            mapController.setZoom(15.0)
                            mapController.animateTo(GeoPoint(location.latitude, location.longitude))
                            firstOpen = false
                        }
                        Log.d("kek", (it.value as Location).latitude.toString())
                        (binding.map.overlays[0] as CustomMe).setLocation(location)
                        binding.map.visibility = View.VISIBLE
                    }
                    is ResponseSplash.Failure -> Log.d("kek", " что то случилось")
                    else -> {}
                }
            }
        }
    }

    private fun intiFlowError(){
        lifecycleScope.launchWhenResumed {
            viewModelMain.sharedStateFlowError.collect {
                when(it){
                    is ErrorApp.FailureDataBase ->{ Toast.makeText(context,
                        it.value.toString(),
                        Toast.LENGTH_LONG)
                        .show()}
                    is ErrorApp.FailureUnknown ->{ Toast.makeText(context,
                        it.value.toString(),
                        Toast.LENGTH_LONG)
                        .show()}
                }
            }
        }
    }

    private fun initFlowDatabase() {
        lifecycleScope.launchWhenResumed {
            viewModelMain.responseDataBaseAttracitonStateFlow.collect {
                when (it) {
                    is ResponseDataBase.Success -> {
                        ifSuccess(it.value)
                    }
                    is ResponseDataBase.Failure -> {
                        Toast.makeText(context,
                            "Ошибка выгрузки данных",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                    is ResponseDataBase.Empty -> {
                        Toast.makeText(context,
                            "В базе данных пусто",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                    is ResponseDataBase.Clear -> {}
                }
            }
        }
    }


    private fun ifSuccess(it: Any?) {
        it.tryCast<List<Attraction>> {
            Toast.makeText(context,
                "Данные есть Attraction   $this",
                Toast.LENGTH_LONG)
                .show()
        }
        it.tryCast<List<PhotoZone>> {
            Toast.makeText(context,
                "Данные есть PhotoZone",
                Toast.LENGTH_LONG)
                .show()
        }
        it.tryCast<List<UserPoint>> {
            Toast.makeText(context,
                "Данные есть UserPoint",
                Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun paintAttraction(listAttraction: List<Attraction>) {

    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause();
        Log.d("Main", "onPause")
        LocationService.stopService(this)
        LocationService.customUnbindService(this, this)
        locationService?.locationServiceListener = null
        locationService = null
        viewModelMain.locationFlow = null
        locationUpdatesJob!!.cancel()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume();
        mapController = binding.map.controller
        LocationService.startLocation(this)
        LocationService.customBindService(this, this)
    }

    fun removeSystemInsets(view: View, listener: OnSystemInsetsChangedListener) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val desiredBottomInset = calculateDesiredBottomInset(
                view,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetBottom,
                listener
            )
            ViewCompat.onApplyWindowInsets(
                view,
                insets.replaceSystemWindowInsets(0, 0, 0, desiredBottomInset)
            )
        }
    }

    private fun isKeyboardAppeared(bottomInset: Int) =
        bottomInset / resourdisplayMetrics.heightPixels.toDouble() > .25

    fun calculateDesiredBottomInset(
        view: View,
        topInset: Int,
        bottomInset: Int,
        listener: OnSystemInsetsChangedListener,
    ): Int {
        val hasKeyboard = isKeyboardAppeared(bottomInset)
        val desiredBottomInset = if (hasKeyboard) bottomInset else 0
        listener(topInset, if (hasKeyboard) 0 else bottomInset)
        return desiredBottomInset
    }


    private fun setWindowTransparency(
        listener: OnSystemInsetsChangedListener = { _, _ -> },
    ) {
        removeSystemInsets(window.decorView, listener)
        //window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d("onServiceConnected", "service подключен")
        service as LocationService.LocationServiceBinder
        locationService = service.getService()
        viewModelMain.locationFlow = locationService?.locationFlow
        initFlowMylocation()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d("onServiceDisconnected", "service onServiceDisconnected")
        locationService = null
        viewModelMain.locationFlow = null
    }
}
typealias OnSystemInsetsChangedListener =
            (statusBarSize: Int, navigationBarSize: Int) -> Unit