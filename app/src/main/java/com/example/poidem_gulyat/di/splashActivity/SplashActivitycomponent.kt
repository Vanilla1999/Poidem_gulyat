package com.example.poidem_gulyat.di.splashActivity

import android.content.Context
import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.SplashActivityScope
import com.example.poidem_gulyat.ui.splashFragment.SplashActivity
import dagger.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named




@SplashActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
)
interface SplashActivitycomponent {
    fun inject(acitivity:SplashActivity)
    @Component.Factory
    interface Factory {

        fun create(applicationComponent: ApplicationComponent): SplashActivitycomponent
    }
}
