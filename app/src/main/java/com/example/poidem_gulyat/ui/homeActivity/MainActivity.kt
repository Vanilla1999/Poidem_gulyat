package com.example.poidem_gulyat.ui.homeActivity
import android.graphics.Color
import android.os.Bundle
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
import android.view.ViewGroup
import com.example.poidem_gulyat.utils.BaseActivity


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val resourdisplayMetrics :DisplayMetrics by lazy { resources.displayMetrics }
    lateinit var navView: BottomNavigationView
    override fun onAfterRequestPermission() {
      //  TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )

        )

        navView.setupWithNavController(navController)

        setWindowTransparency { _, navigationBarSize ->
            navView.setPadding(0, 0, 0, navigationBarSize)
        }

    }

    fun removeSystemInsets(view: View,listener: OnSystemInsetsChangedListener) {
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
        listener: OnSystemInsetsChangedListener
    ): Int {
        val hasKeyboard = isKeyboardAppeared( bottomInset)
        val desiredBottomInset = if (hasKeyboard) bottomInset else 0
        listener(topInset, if (hasKeyboard) 0 else bottomInset)
        return desiredBottomInset
    }


    private fun setWindowTransparency(
        listener: OnSystemInsetsChangedListener = { _, _ -> }
    ) {
        removeSystemInsets(window.decorView,listener)
        //window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
    }
}
typealias OnSystemInsetsChangedListener =
            (statusBarSize: Int, navigationBarSize: Int) -> Unit