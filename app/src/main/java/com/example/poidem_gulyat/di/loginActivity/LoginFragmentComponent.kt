package com.example.poidem_gulyat.di.loginActivity

import android.content.Context
import com.example.poidem_gulyat.di.ActivityScope
import com.example.poidem_gulyat.di.AppScope
import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.ui.login.LoginActivity
import com.example.poidem_gulyat.ui.login.LoginFragment
import com.example.poidem_gulyat.ui.splashFragment.SplashActivity
import dagger.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named




@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
)
interface LoginFragmentComponent {
    fun inject(fragment:LoginFragment)
    @Component.Factory
    interface Factory {

        fun create(applicationComponent: ApplicationComponent): LoginFragmentComponent
    }
}
