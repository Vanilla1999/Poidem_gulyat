package com.example.poidem_gulyat.di.mainActivtiy


import com.example.poidem_gulyat.di.HomeScope
import com.example.poidem_gulyat.ui.homeActivity.infoFragment.InfoFragment
import dagger.Component

@HomeScope
@Component(
    dependencies = [MainActvitityComponent::class],
)
interface InfoFragmentComponent {
    fun inject(fragment: InfoFragment)
    @Component.Factory
    interface Factory {
        fun create(applicationComponent: MainActvitityComponent): InfoFragmentComponent
    }
}