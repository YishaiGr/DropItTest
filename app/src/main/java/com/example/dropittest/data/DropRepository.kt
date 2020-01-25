package com.example.dropittest.data

import com.example.dropittest.data.entities.Contact
import com.example.dropittest.data.entities.Drop
import com.example.dropittest.data.entities.ShippingAddress
import com.example.dropittest.data.network.DropItService
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DropRepository @Inject constructor(private val client: DropItService) {

    fun getContact(): Call<Contact> {
        return client.getContact()
    }

    fun updateContact(contact: Contact): Call<ResponseBody>{
        return client.updateContact(contact.fullName, contact.phoneNumber)
    }

    fun getAddress(): Call<ShippingAddress>{
        return client.getAddress()
    }

    fun updateAddress(shippingAddress: ShippingAddress): Call<ResponseBody>{
        return client.updateAddress(shippingAddress.streetAddress, shippingAddress.city, shippingAddress.country)
    }

    fun drop(dropItem :Drop): Call<Drop>{
        return client.drop(dropItem.address, dropItem.contact, dropItem.bags)
    }
}