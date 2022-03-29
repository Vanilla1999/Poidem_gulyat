package com.example.poidem_gulyat.di.mainActivtiy


import com.example.poidem_gulyat.di.HomeScope
import com.example.poidem_gulyat.ui.homeActivity.dashboard.listFilter.ListFilterFragment
import dagger.Component

@HomeScope
@Component(
    dependencies = [MainActvitityComponent::class],
)
interface ListFilterFragmentComponent {
    fun inject(fragment: ListFilterFragment)
    @Component.Factory
    interface Factory {
        fun create(applicationComponent: MainActvitityComponent): ListFilterFragmentComponent
    }
}