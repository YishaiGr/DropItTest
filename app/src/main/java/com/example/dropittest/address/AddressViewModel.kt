package com.example.dropittest.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dropittest.data.DropRepository
import com.example.dropittest.data.entities.ShippingAddress
import com.example.dropittest.util.Event
import com.example.dropittest.util.ResponseContent
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class AddressViewModel @Inject constructor(private val repository: DropRepository) : ViewModel() {
    private val _addressLiveData by lazy { MutableLiveData<ResponseContent<ShippingAddress>>() }
    val addressLiveData: LiveData<ResponseContent<ShippingAddress>> = _addressLiveData
    private val _updateAddressLiveData by lazy { MutableLiveData<Event<ResponseContent<Boolean>>>() }
    val updateAddressLiveData: LiveData<Event<ResponseContent<Boolean>>> = _updateAddressLiveData
    private val _isoCodesLiveData by lazy { MutableLiveData<Array<String>>(Locale.getISOCountries()) }
    val isoCodesLiveData: LiveData<Array<String>> = _isoCodesLiveData

    fun getAddress() {
        repository.getAddress().enqueue(object : Callback<ShippingAddress> {
            override fun onFailure(call: Call<ShippingAddress>, t: Throwable) {
                _addressLiveData.value = ResponseContent(null, t.message)
            }

            override fun onResponse(call: Call<ShippingAddress>, response: Response<ShippingAddress>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let { shippingAddress ->
                            _addressLiveData.value = ResponseContent(shippingAddress)
                        }
                    }
                    else -> _addressLiveData.value = ResponseContent(null, response.message())
                }
            }
        })
    }

    fun updateAddress(shippingAddress: ShippingAddress) {
        repository.updateAddress(shippingAddress).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _updateAddressLiveData.value = Event(ResponseContent(null, t.message))
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {_ ->
                            _updateAddressLiveData.value = Event(ResponseContent(true))
                        }
                    }
                    else -> _updateAddressLiveData.value = Event(ResponseContent(null, response.message()))
                }
            }
        })
    }
}