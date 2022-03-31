package com.example.poidem_gulyat.di.mainActivtiy

import android.content.Context
import com.example.poidem_gulyat.data.repository.filter.FiltersRepository
import com.example.poidem_gulyat.data.repository.filter.FiltersRepositoryImpl
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.markers.*
import com.example.poidem_gulyat.data.source.gps.GpsDataSource
import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.ApplicationContext
import com.example.poidem_gulyat.di.MainActivityScope
import com.example.poidem_gulyat.ui.homeActivity.MainActivity
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides


@Module
class MainModule {
    @Provides
    @MainActivityScope
    fun provideMarkerManager(
         markerRepository: MarkerRepository,
         filtersRepository: FiltersRepository
    ):MarkerManager{
        return MarkerManager(markerRepository,filtersRepository)
    }
}

@Module
interface BindingModule {

    @MainActivityScope
    @Suppress("FunctionName")
    @Binds
    fun bindsMarkserRepository_to_MarkerRepositoryImpl(userPointRepositoryImpl: MarkerRepositoryImpl): MarkerRepository

    @MainActivityScope
    @Suppress("FunctionName")
    @Binds
    fun bindsFilterRepository_to_FilterRepositoryImpl(filtersRepositoryImpl: FiltersRepositoryImpl): FiltersRepository
}

@MainActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [MainModule::class, BindingModule::class]
)
interface MainActvitityComponent {

    fun provideMarkerManager():MarkerManager

    fun provideGpsRepo(): GpsRepository

    fun provideGpsSource(): GpsDataSource

    @Suppress("FunctionName")
    fun bindsMarkserRepository_to_MarkerRepositoryImpl(): MarkerRepository

    @Suppress("FunctionName")
    fun bindsFilterRepository_to_FilterRepositoryImpl(): FiltersRepository

    @ApplicationContext
    fun getApplication(): Context

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(applicationComponent: ApplicationComponent): MainActvitityComponent
    }
}