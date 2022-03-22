package com.example.poidem_gulyat.di.mainActivtiy


import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.HomeScope
import com.example.poidem_gulyat.ui.homeActivity.dashboard.FilterFragment
import com.example.poidem_gulyat.ui.homeActivity.home.HomeFragment
import dagger.Component

@HomeScope
@Component(
    dependencies = [MainActvitityComponent::class],
)
interface FilterFragmentComponent {
    fun inject(fragment: FilterFragment)
    @Component.Factory
    interface Factory {
        fun create(applicationComponent: MainActvitityComponent): FilterFragmentComponent
    }
}