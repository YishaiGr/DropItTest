package com.example.dropittest.di

import android.content.Context
import com.example.dropittest.main.DropComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class, SubComponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun dropComponent(): DropComponent.Factory
}