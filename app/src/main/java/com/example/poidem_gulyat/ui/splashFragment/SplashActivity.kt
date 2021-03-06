package com.example.poidem_gulyat.ui.splashFragment

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.TextView
import androidx.activity.viewModels
import com.example.poidem_gulyat.App.Companion.appComponentMain
import com.example.poidem_gulyat.databinding.ActivitySplashBinding
import com.example.poidem_gulyat.di.splashActivity.DaggerSplashActivitycomponent
import com.example.poidem_gulyat.di.splashActivity.SplashActivitycomponent
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import com.example.poidem_gulyat.ui.login.LoginActivity
import com.example.poidem_gulyat.utils.startNewActivity
import com.example.poidem_gulyat.data.ResponseSplash
import com.example.poidem_gulyat.utils.BaseActivity
import com.example.poidem_gulyat.utils.PermissionTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), CoroutineScope {

    @Inject
    lateinit var factory: SplashModel.FactorySplash
    private val viewModelSplash by viewModels<SplashModel> { factory }

    lateinit var appComponent: SplashActivitycomponent
    private lateinit var binding: ActivitySplashBinding
    private lateinit var fullscreenContent: TextView
    private val hideHandler = Handler()

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    private val hideRunnable = Runnable { hide() }
    override fun onAfterRequestPermission() {
        initialFlow()
    }

    private fun initialFlow() {
        launch {
            viewModelSplash.prefStateFlow.collect {
                when (it) {
                    is ResponseSplash.Success -> startNewActivity(MainActivity::class.java)
                    is ResponseSplash.Failure -> {
                        Log.d("SplashActivity", " null ")
                        finish()
                        startNewActivity(LoginActivity::class.java)
                    }
                    else -> {}
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = DaggerSplashActivitycomponent.factory().create(appComponentMain)
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent
        viewModelSplash.checkUser()
        delayedStart(1000)

    }

    private fun hide() {
        hidePart2Runnable
        if(allPermissionGrandted())
            initialFlow()
        //startNewActivity(MainActivity::class.java)
    }

    private fun delayedStart(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()
}