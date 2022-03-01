package com.example.poidem_gulyat.ui.login

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.poidem_gulyat.App.Companion.appComponentMain
import com.example.poidem_gulyat.R



class LoginActivity: AppCompatActivity() {
    private val resourdisplayMetrics : DisplayMetrics by lazy { resources.displayMetrics }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setWindowTransparency ()
    }
    fun removeSystemInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->

            val desiredBottomInset = calculateDesiredBottomInset(
                view,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetBottom,
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
    ): Int {
        val hasKeyboard = isKeyboardAppeared(bottomInset)
        return if (hasKeyboard) bottomInset else 0
    }


    private fun setWindowTransparency(
    ) {
        removeSystemInsets(window.decorView)
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
    }
}