package com.example.poidem_gulyat.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import java.lang.annotation.RetentionPolicy
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.reflect.KClass


@Scope
annotation class ActivityScope
@Scope
annotation class FragmentScope
@Scope
annotation class AppScope
@Qualifier
annotation class SplashAcitivityContext
@Qualifier
annotation class ApplicationContext
//@Named
//annotation class ApplicationContext





