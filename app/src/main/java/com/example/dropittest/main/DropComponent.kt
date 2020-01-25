package com.example.dropittest.main

import com.example.dropittest.address.AddressFragment
import com.example.dropittest.bags.BagsFragment
import com.example.dropittest.contact.ContactFragment
import com.example.dropittest.di.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface DropComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): DropComponent
    }

    fun inject(activity: DropActivity)
    fun inject(fragment: ContactFragment)
    fun inject(fragment: AddressFragment)
    fun inject(fragment: BagsFragment)
}