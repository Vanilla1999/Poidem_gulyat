package com.example.poidem_gulyat.di.mainActivtiy

import android.content.Context
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.hardware.GpsRepositoryImpl
import com.example.poidem_gulyat.data.repository.location.UserLocationRepository
import com.example.poidem_gulyat.data.repository.location.UserLocationRepositoryImpl
import com.example.poidem_gulyat.data.repository.markers.*
import com.example.poidem_gulyat.data.source.gps.GpsDataSource
import com.example.poidem_gulyat.data.source.gps.GpsDataSourceImpl
import com.example.poidem_gulyat.di.ApplicationComponent
import com.example.poidem_gulyat.di.ApplicationContext
import com.example.poidem_gulyat.di.MainActivityScope
import com.example.poidem_gulyat.ui.login.LoginFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableStateFlow


@Module
interface MainModule {
}

@Module
interface BindingModule {
    @MainActivityScope
    @Suppress("FunctionName")
    @Binds
    fun bindsAttractionRepository_to_AttractionRepositoryImpl(attractionRepositoryImpl: AttractionRepositoryImpl): AttractionRepository

    @MainActivityScope
    @Suppress("FunctionName")
    @Binds
    fun bindsPhotoZoneRepository_to_PhotoZoneRepositoryImpl(photoZoneRepositoryImpl: PhotoZoneRepositoryImpl): PhotoZoneRepository

    @MainActivityScope
    @Suppress("FunctionName")
    @Binds
    fun bindsUserPointRepository_to_UserPointRepositoryImpl(userPointRepositoryImpl: UserPointRepositoryImpl): UserPointRepository
}

@MainActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [MainModule::class, BindingModule::class]
)
interface MainActvitityComponent {

    fun provideGpsRepo(): GpsRepository

    fun provideGpsSource(): GpsDataSource

    @Suppress("FunctionName")
    fun bindsAttractionRepository_to_AttractionRepositoryImpl(): AttractionRepository

    @Suppress("FunctionName")
    fun bindsPhotoZoneRepository_to_PhotoZoneRepositoryImpl(): PhotoZoneRepository

    @Suppress("FunctionName")
    fun bindsUserPointRepository_to_UserPointRepositoryImpl(): UserPointRepository

    @ApplicationContext
    fun getApplication(): Context

    @Component.Factory
    interface Factory {
        fun create(applicationComponent: ApplicationComponent): MainActvitityComponent
    }
}