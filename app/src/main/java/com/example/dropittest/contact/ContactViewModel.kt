package com.example.dropittest.contact

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dropittest.R
import com.example.dropittest.data.DropRepository
import com.example.dropittest.data.entities.Contact
import com.example.dropittest.util.Event
import com.example.dropittest.util.ResponseContent
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ContactViewModel @Inject constructor(private val repository: DropRepository, private val context: Context) : ViewModel() {

    private val _contactLiveData  by lazy { MutableLiveData<ResponseContent<Contact>>() }
    val contactLiveData: LiveData<ResponseContent<Contact>> = _contactLiveData
    private val _updateContactLiveData by lazy { MutableLiveData<Event<ResponseContent<Boolean>>>() }
    val updateContactLiveData: LiveData<Event<ResponseContent<Boolean>>> = _updateContactLiveData
    private val _countryCodesLiveData by lazy { MutableLiveData<Array<String>>(getCountryCodes()) }
    val countryCodesLiveData: LiveData<Array<String>> = _countryCodesLiveData

    private fun getCountryCodes(): Array<String>{
        val countryCodes: Array<String> = context.resources.getStringArray(R.array.countryCodes)
        val codes = Array(countryCodes.size) { "" }
        for (i in countryCodes.indices) {
            val countryAndCodes = countryCodes[i].split(",")
            codes[i] = countryAndCodes[0]
        }
        return codes
    }

    fun getContact() {
        repository.getContact().enqueue(object : Callback<Contact> {
            override fun onFailure(call: Call<Contact>, t: Throwable) {
                _contactLiveData.value = ResponseContent(null, t.message)
            }

            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let { contact ->
                            _contactLiveData.value = ResponseContent(contact)
                        }
                    }
                    else -> _contactLiveData.value = ResponseContent(null, response.message())
                }
            }
        })
    }

    fun updateContact(contact: Contact) {
        repository.updateContact(contact).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _updateContactLiveData.value = Event(ResponseContent(null, t.message))
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {_ ->
                            _updateContactLiveData.value = Event(ResponseContent(true))
                        }
                    }
                    else -> _updateContactLiveData.value = Event(ResponseContent(null, response.message()))
                }
            }
        })
    }
}
