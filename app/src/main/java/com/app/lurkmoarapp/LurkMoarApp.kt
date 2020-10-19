package com.app.lurkmoarapp

import android.app.Activity
import android.app.Application
import com.app.lurkmoarapp.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class LurkMoarApp : Application(), HasActivityInjector {

    @Inject
    lateinit var appInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppInjector.init(this)
    }

    override fun activityInjector() = appInjector
}

