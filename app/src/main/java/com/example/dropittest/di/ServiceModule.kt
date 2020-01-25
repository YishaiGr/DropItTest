package com.example.dropittest.di

import com.example.dropittest.data.network.DropItClient
import com.example.dropittest.data.network.DropItService
import dagger.Module
import dagger.Provides

@Module
class ServiceModule {

    @Provides
    fun provideClientService(): DropItService{
        return DropItClient.createClient()
    }
}