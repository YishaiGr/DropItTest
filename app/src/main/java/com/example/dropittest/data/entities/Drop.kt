package com.example.dropittest.data.entities

import com.google.gson.annotations.SerializedName

data class Drop (
    val address: ShippingAddress,
    @SerializedName("contac details")
    val contact: Contact,
    val bags: List<String>
)