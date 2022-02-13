package com.example.poidem_gulyat

import android.app.Application
import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.DaggerApplicationComponent

class App() : Application() {

    override fun onCreate() {
        appComponentMain = DaggerApplicationComponent.factory().create(this)
        super.onCreate()
    }
    companion object {
        lateinit var appComponentMain: ApplicationComponent
    }
}