package com.example.poidem_gulyat.di.mainActivtiy


import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.HomeScope
import com.example.poidem_gulyat.ui.homeActivity.home.HomeFragment
import dagger.Component

@HomeScope
@Component(
    dependencies = [ApplicationComponent::class],
)
interface HomeFragmentComponent {
    fun inject(fragment: HomeFragment)
    @Component.Factory
    interface Factory {

        fun create(applicationComponent: ApplicationComponent): HomeFragmentComponent
    }
}