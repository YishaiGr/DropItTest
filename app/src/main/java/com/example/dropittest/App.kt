package com.example.dropittest

import android.app.Application
import com.example.dropittest.di.AppComponent
import com.example.dropittest.di.DaggerAppComponent

class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}