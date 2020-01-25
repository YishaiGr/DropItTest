package com.example.dropittest.address

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.dropittest.main.DropActivity
import com.example.dropittest.main.DropViewModel
import com.example.dropittest.R
import com.example.dropittest.data.entities.ShippingAddress
import com.example.dropittest.databinding.AddressFragmentBinding
import kotlinx.android.synthetic.main.address_fragment.*
import java.util.*
import javax.inject.Inject

class AddressFragment : Fragment() {

    @Inject lateinit var addressViewModel: AddressViewModel
    private lateinit var dropViewModel: DropViewModel
    private lateinit var dataAdapter: ArrayAdapter<String>
    private lateinit var isoCountryCodes: Array<String>
    private lateinit var countryNames: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            (it as DropActivity).dropComponent.inject(this)
            dropViewModel = it.dropViewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding:AddressFragmentBinding  = DataBindingUtil.inflate(inflater, R.layout.address_fragment, container, false)
        binding.addressViewModel = addressViewModel
        binding.dropViewModel = dropViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dropViewModel.enableBackButton(true)
        dropViewModel.setTitle(getString(R.string.address_title))
        dropViewModel.enableLoading(false)
        setFields()
        setObservers()
    }

    private fun setFields(){
        isoCountryCodes = Locale.getISOCountries()
        countryNames = Array(isoCountryCodes.size) { "" }
        for (i in isoCountryCodes.indices) {
            val locale = Locale("", isoCountryCodes[i])
            val countryName: String = locale.displayCountry
            countryNames[i] = countryName
        }
        context?.let {
            dataAdapter = ArrayAdapter(it, R.layout.country_spinner_item, countryNames)
            dataAdapter.setDropDownViewResource(R.layout.country_spinner_item)
            country_address_spinner.adapter = dataAdapter
        }
        street_address_address_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    if (text.trim().length>4)
                        street_address_address_et.setBackgroundResource(R.drawable.edit_text_green_bg)
                    else
                        street_address_address_et.setBackgroundResource(R.drawable.edit_text_red_bg)
                    setButtonText()
                }
            }
        })
        city_address_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    if (text.trim().length>4)
                        city_address_et.setBackgroundResource(R.drawable.edit_text_green_bg)
                    else
                        city_address_et.setBackgroundResource(R.drawable.edit_text_red_bg)
                    setButtonText()
                }
            }
        })
        country_address_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                setButtonText()
            }
        }
        next_address_btn.setOnClickListener {
            if (city_address_et.text.toString().trim().length>4 && street_address_address_et.text.toString().trim().length>4){
                dropViewModel.enableLoading(true)
                next_address_btn.setBackgroundResource(R.drawable.button_green_bg)
                next_address_btn.setTextColor(resources.getColor(R.color.white))
                val streetAddress = street_address_address_et.text.toString()
                val city = city_address_et.text.toString()
                val countryPosition = country_address_spinner.selectedItemPosition
                val shippingAddress = ShippingAddress(streetAddress, city, isoCountryCodes[countryPosition])
                addressViewModel.updateAddress(shippingAddress)
                dropViewModel.address = shippingAddress
            }
        }
    }

    private fun setObservers(){
        addressViewModel.getAddress()
        dropViewModel.enableLoading(true)
        addressViewModel.updateAddressLiveData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {response ->
                dropViewModel.enableLoading(false)
                if (response.content==true)
                    next_address_btn.findNavController().navigate(R.id.action_addressFragment_to_bagsFragment)
                else
                    dropViewModel.handleError(response.errorMessage)
            }
        })
    }

    private fun setButtonText(){
        dropViewModel.savedShippingAddress?.let { shippingAddress ->
            if (street_address_address_et.text.toString()!=shippingAddress.streetAddress
                || city_address_et.text.toString()!=shippingAddress.city
                || isoCountryCodes[country_address_spinner.selectedItemPosition]!=shippingAddress.country){
                next_address_btn.text = getString(R.string.update_button_text)
            }
            else {
                next_address_btn.text = getString(R.string.next_button)
            }
        }
    }
}