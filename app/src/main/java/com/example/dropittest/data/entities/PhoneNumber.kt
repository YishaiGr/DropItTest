package com.example.dropittest.data.entities

import com.google.gson.annotations.Expose

data class PhoneNumber(
    @Expose
    val countryCode: Int,
    @Expose
    val number: String
)