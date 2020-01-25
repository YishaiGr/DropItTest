package com.example.dropittest.util

import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dropittest.R
import com.example.dropittest.data.entities.Contact
import com.example.dropittest.data.entities.Drop
import com.example.dropittest.data.entities.ShippingAddress
import com.example.dropittest.main.DropViewModel
import com.example.dropittest.review.ReviewBagAdapter

@BindingAdapter("android:text")
fun setErrorMessage(textView: TextView, message: String?){
    message?.let {
        if (message.contains(textView.context.getString(R.string.unable_resolve_host)))
            textView.text = textView.context.getString(R.string.unable_resolve_host)
        else
            textView.text = message
    }
}

fun receivedDataFromAPI(responseContent: ResponseContent<Any>?,
                        dropViewModel: DropViewModel){
    dropViewModel.enableLoading(false)
    if (responseContent?.content == null)
        dropViewModel.handleError(responseContent?.errorMessage)
}

@BindingAdapter(value = ["isoCountryCodes", "address", "dropViewModel"], requireAll = true)
fun setAddressSpinner(spinner: Spinner, isoCodes: Array<String>,
                      responseContent: ResponseContent<ShippingAddress>?,
                      dropViewModel: DropViewModel){
    receivedDataFromAPI(responseContent, dropViewModel)
    responseContent?.content?.let { shippingAddress ->
        val position = isoCodes.indexOf(shippingAddress.country)
        spinner.setSelection(position)
    }
}

@BindingAdapter(value = ["android:text", "dropViewModel"], requireAll = true)
fun setAddressEditTests(editText: EditText,
                        responseContent: ResponseContent<ShippingAddress>?,
                        dropViewModel: DropViewModel){
    receivedDataFromAPI(responseContent, dropViewModel)
    responseContent?.content?.let { shippingAddress ->
        dropViewModel.savedShippingAddress = shippingAddress
        when (editText.id) {
            R.id.street_address_address_et -> editText.setText(shippingAddress.streetAddress)
            R.id.city_address_et -> editText.setText(shippingAddress.city)
        }
    }
}

@BindingAdapter(value = ["countryCodes", "contact", "dropViewModel"], requireAll = true)
fun setContactSpinner(spinner: Spinner, codes: Array<String>,
                      responseContent: ResponseContent<Contact>?,
                      dropViewModel: DropViewModel){
    receivedDataFromAPI(responseContent, dropViewModel)
    responseContent?.content?.let { contact ->
        val position = codes.indexOf(contact.phoneNumber.countryCode.toString())
        if (position >= 0)
            spinner.setSelection(position)
    }
}

@BindingAdapter(value = ["android:text", "dropViewModel"], requireAll = true)
fun setContactEditTexts(editText: EditText,
                        responseContent: ResponseContent<Contact>?,
                        dropViewModel: DropViewModel) {
    receivedDataFromAPI(responseContent, dropViewModel)
    responseContent?.content?.let { contact ->
        dropViewModel.savedContact = contact
        when (editText.id) {
            R.id.full_name_contact_et -> editText.setText(contact.fullName)
            R.id.phone_number_contact_et -> editText.setText(contact.phoneNumber.number)
        }
    }
}

@BindingAdapter(value = ["android:text", "dropViewModel"], requireAll = true)
fun reviewDrop(textView: TextView,
               responseContent: ResponseContent<Drop>?,
               dropViewModel: DropViewModel) {
    receivedDataFromAPI(responseContent, dropViewModel)
    responseContent?.content?.let { drop ->
        when (textView.id) {
            R.id.full_name_review_tv -> textView.text = drop.contact.fullName
            R.id.phone_review_tv -> textView.text = textView.context.getString(
                R.string.phone_number_review,
                drop.contact.phoneNumber.countryCode,
                drop.contact.phoneNumber.number
            )
            R.id.address_review_tv -> textView.text = drop.address.streetAddress
            R.id.city_review_tv -> textView.text = drop.address.city
            R.id.country_review_tv -> textView.text = drop.address.country
        }
    }
}

@BindingAdapter(value = ["setupRecycler", "dropViewModel"], requireAll = true)
fun setupRecycler(rv: RecyclerView,
                  responseContent: ResponseContent<Drop>?,
                  dropViewModel: DropViewModel) {
    receivedDataFromAPI(responseContent, dropViewModel)
    responseContent?.content?.let { drop ->
        rv.layoutManager = LinearLayoutManager(rv.context, RecyclerView.VERTICAL, false)
        rv.adapter = ReviewBagAdapter(drop.bags as ArrayList<String>)
    }
}