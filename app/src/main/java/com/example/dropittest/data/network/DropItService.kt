package com.example.dropittest.data.network

import com.example.dropittest.data.entities.Contact
import com.example.dropittest.data.entities.Drop
import com.example.dropittest.data.entities.PhoneNumber
import com.example.dropittest.data.entities.ShippingAddress
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface DropItService {

    @GET("user/contact")
    fun getContact(): Call<Contact>

    @POST("user/contact")
    @FormUrlEncoded
    fun updateContact(
        @Field("fullName") fullName: String,
        @Field("phoneNumber") phoneNumber: PhoneNumber
    ): Call<ResponseBody>

    @GET("user/address")
    fun getAddress(): Call<ShippingAddress>

    @POST("user/address")
    @FormUrlEncoded
    fun updateAddress(@Field("streetAddress") streetAddress: String,
                      @Field("city") city: String,
                      @Field("country") country: String): Call<ResponseBody>

    @POST("drop")
    @FormUrlEncoded
    fun drop(
        @Field("address") shippingAddress: ShippingAddress,
        @Field("contac details") contact: Contact,
        @Field("bags") bags: List<String>
    ): Call<Drop>
}