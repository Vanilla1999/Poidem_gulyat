package com.example.poidem_gulyat.di.locationService

import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.hardware.GpsRepositoryImpl
import com.example.poidem_gulyat.data.source.gps.GpsDataSource
import com.example.poidem_gulyat.data.source.gps.GpsDataSourceImpl
import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.LocationServiceScope
import com.example.poidem_gulyat.di.LoginActivityScope
import com.example.poidem_gulyat.di.MainActivityScope
import com.example.poidem_gulyat.services.LocationService
import com.example.poidem_gulyat.ui.login.LoginFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Module
interface  MainModule {
}

@LocationServiceScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [MainModule::class]
)
interface LocationServiceComponent {
    fun inject(fragment: LocationService)
    @Component.Factory
    interface Factory {
        fun create(applicationComponent: ApplicationComponent): LocationServiceComponent
    }
}