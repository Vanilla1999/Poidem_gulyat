package com.example.poidem_gulyat.di

import android.content.Context
import com.otus.myapplication.crypto.Keys
import com.otus.myapplication.crypto.Security
import com.otus.securehomework.data.source.local.UserPreferences
import com.otus.securehomework.data.source.network.AuthApi
import com.otus.securehomework.data.source.network.UserApi
import com.otus.securehomework.di.RemoteDataSource
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named
import javax.inject.Singleton


@Module()
class ApplicationModule {


    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource,
    ): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java)
    }

    @Provides
    fun provideUserApi(
        remoteDataSource: RemoteDataSource,
    ): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java)
    }
    // бд должна быть.
}



@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun provideAuthApi(): AuthApi

    fun provideUserApi(): UserApi

    @ApplicationContext
    fun getApplication():Context

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @ApplicationContext context: Context): ApplicationComponent
    }
}
