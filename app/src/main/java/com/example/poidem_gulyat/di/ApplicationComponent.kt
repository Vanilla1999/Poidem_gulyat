package com.example.poidem_gulyat.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.poidem_gulyat.BuildConfig
import com.example.poidem_gulyat.data.repository.hardware.GpsRepository
import com.example.poidem_gulyat.data.repository.hardware.GpsRepositoryImpl
import com.example.poidem_gulyat.data.repository.location.UserLocationRepository
import com.example.poidem_gulyat.data.repository.location.UserLocationRepositoryImpl
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import com.example.poidem_gulyat.data.source.gps.GpsDataSource
import com.example.poidem_gulyat.data.source.gps.GpsDataSourceImpl
import com.otus.securehomework.data.source.network.AuthApi
import com.otus.securehomework.data.source.network.UserApi
import com.otus.securehomework.di.RemoteDataSource
import dagger.*


@Module()
class ApplicationModule {

    @Provides
     fun getRoomDatabase(
        @ApplicationContext
        context: Context): DatabaseMain {
        val builder = Room.databaseBuilder(
            context, DatabaseMain::class.java, DatabaseMain.DATABASE
        )
        return builder.build()
    }

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

@Module
interface  LocationModule {

    @Binds
    fun bindsGpsDataSource_to_GpsDataSourceImpl(gpsDataSourceImpl: GpsDataSourceImpl): GpsDataSource


    @Binds
    fun bindsGpsRepo_to_GpsRepoImpl(gpsRepositoryImpl: GpsRepositoryImpl): GpsRepository

    @Binds
    fun bindsUserLocationRepository_to_UserLocationRepositoryImpl(userLocationRepositoryImpl: UserLocationRepositoryImpl): UserLocationRepository
}

@Component(modules = [ApplicationModule::class,LocationModule::class])
interface ApplicationComponent {

    fun provideAuthApi(): AuthApi

    fun provideUserApi(): UserApi

    fun getRoomDatabase(): DatabaseMain

    fun bindsGpsDataSource_to_GpsDataSourceImpl(): GpsRepository

    fun bindsGpsRepo_to_GpsRepoImpl(): GpsDataSource

    fun bindsUserLocationRepository_to_UserLocationRepositoryImpl():UserLocationRepository

    @ApplicationContext
    fun getApplication():Context

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @ApplicationContext context: Context): ApplicationComponent
    }
}
