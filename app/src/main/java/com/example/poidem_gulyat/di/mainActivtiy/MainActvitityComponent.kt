package com.example.poidem_gulyat.di.mainActivtiy

import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.hardware.GpsRepositoryImpl
import com.example.poidem_gulyat.data.source.gps.GpsDataSource
import com.example.poidem_gulyat.data.source.gps.GpsDataSourceImpl
import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.MainActivityScope
import com.example.poidem_gulyat.ui.login.LoginFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableStateFlow


@Module
interface  MainModule {
}

@MainActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [MainModule::class]
)
interface MainActvitityComponent {

    fun provideGpsRepo(): GpsRepository

    fun provideGpsSource(): GpsDataSource

    @Component.Factory
    interface Factory {
        fun create(applicationComponent: ApplicationComponent): MainActvitityComponent
    }
}