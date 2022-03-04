package com.example.poidem_gulyat.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import java.lang.annotation.RetentionPolicy
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.reflect.KClass


@Scope
annotation class LoginActivityScope
@Scope
annotation class FragmentScope
@Scope
annotation class HomeScope
@Scope
annotation class MainActivityScope

@Scope
annotation class LocationServiceScope

@Scope
annotation class SplashActivityScope
@Qualifier
annotation class SplashAcitivityContext
@Qualifier
annotation class MainActivityContext
@Qualifier
annotation class ApplicationContext
//@Named
//annotation class ApplicationContext





