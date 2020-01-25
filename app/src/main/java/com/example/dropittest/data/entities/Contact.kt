package com.example.dropittest.data.entities

import com.google.gson.annotations.Expose

data class Contact(
    @Expose
    val fullName: String,
    @Expose
    val phoneNumber: PhoneNumber
)