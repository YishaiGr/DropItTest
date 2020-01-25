package com.example.dropittest.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dropittest.data.DropRepository
import com.example.dropittest.data.entities.Contact
import com.example.dropittest.data.entities.Drop
import com.example.dropittest.data.entities.ShippingAddress
import com.example.dropittest.di.ActivityScope
import com.example.dropittest.util.Event
import com.example.dropittest.util.ResponseContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@ActivityScope
class DropViewModel @Inject constructor(private val repository: DropRepository): ViewModel() {
    var contact: Contact? = null
    var address: ShippingAddress? = null
    var bags: List<String>? = null
    private val _dropLiveData by lazy { MutableLiveData<ResponseContent<Drop>>() }
    val dropLiveData: LiveData<ResponseContent<Drop>> = _dropLiveData
    private val _dropLiveDataOnce by lazy { MutableLiveData<Event<ResponseContent<Boolean>>>() }
    val dropLiveDataOnce: LiveData<Event<ResponseContent<Boolean>>> = _dropLiveDataOnce
    private val _titleLiveData by lazy { MutableLiveData<String>() }
    val titleLiveData: LiveData<String> = _titleLiveData
    private val _handleErrorLiveData by lazy { MutableLiveData<Event<String?>>() }
    val handleErrorLiveData: LiveData<Event<String?>> = _handleErrorLiveData
    private val _enableBackButtonLiveData by lazy { MutableLiveData<Boolean>() }
    val enableBackButtonLiveData: LiveData<Boolean> = _enableBackButtonLiveData
    private val _enableLoadingLiveData by lazy { MutableLiveData<Boolean>() }
    val enableLoadingLiveData: LiveData<Boolean> = _enableLoadingLiveData
    private val _errorMessageLiveData by lazy { MutableLiveData<String>() }
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData
    var savedShippingAddress: ShippingAddress? = null
    var savedContact: Contact? = null

    fun setErrorMessage(message: String){
        _errorMessageLiveData.value = message
    }

    fun enableLoading(on: Boolean){
        _enableLoadingLiveData.value = on
    }

    fun enableBackButton(on: Boolean){
        _enableBackButtonLiveData.value = on
    }

    fun setTitle(title: String){
        _titleLiveData.value = title
    }

    fun handleError(message: String?){
        _handleErrorLiveData.value = Event(message)
    }

    fun drop() {
        contact?.let { contact ->
            address?.let {address ->
                bags?.let {bags ->
                    val dropItem = Drop(address, contact, bags)
                    repository.drop(dropItem).enqueue(object : Callback<Drop>{
                        override fun onFailure(call: Call<Drop>, t: Throwable) {
                            _dropLiveData.value = ResponseContent(null, t.message)
                        }

                        override fun onResponse(call: Call<Drop>, response: Response<Drop>) {
                            when (response.code()) {
                                200 -> {
                                    response.body()?.let { drop ->
                                        _dropLiveData.value = ResponseContent(drop)
                                        _dropLiveDataOnce.value = Event(ResponseContent(true))
                                    }
                                }
                                else -> {
                                    _dropLiveData.value = ResponseContent(null, response.message())
                                    _dropLiveDataOnce.value = Event(ResponseContent(null, response.message()))
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}