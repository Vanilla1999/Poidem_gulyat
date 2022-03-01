package com.example.poidem_gulyat.ui.login

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.poidem_gulyat.App.Companion.appComponentMain
import com.example.poidem_gulyat.R
import com.example.poidem_gulyat.utils.BaseActivity


class LoginActivity: BaseActivity() {
    private val resourdisplayMetrics : DisplayMetrics by lazy { resources.displayMetrics }
    override fun onAfterRequestPermission() {
//        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}