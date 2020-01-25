package com.example.dropittest.contact

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
import com.example.dropittest.R
import com.example.dropittest.data.entities.Contact
import com.example.dropittest.data.entities.PhoneNumber
import com.example.dropittest.databinding.ContactFragmentBinding
import com.example.dropittest.main.DropActivity
import com.example.dropittest.main.DropViewModel
import kotlinx.android.synthetic.main.contact_fragment.*
import javax.inject.Inject

class ContactFragment : Fragment() {

    @Inject
    lateinit var contactViewModel: ContactViewModel
    private lateinit var dropViewModel: DropViewModel
    private lateinit var dataAdapter: ArrayAdapter<String>
    private lateinit var codes: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            (it as DropActivity).dropComponent.inject(this)
            dropViewModel = it.dropViewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ContactFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.contact_fragment, container, false)
        binding.contactViewModel = contactViewModel
        binding.dropViewModel = dropViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dropViewModel.enableBackButton(false)
        dropViewModel.setTitle(getString(R.string.contact_details_title))
        dropViewModel.enableLoading(false)
        setFields()
        setObservers()
        contactViewModel.getContact()
        dropViewModel.enableLoading(true)
    }

    private fun setFields() {
        val countryCodes: Array<String> = resources.getStringArray(R.array.countryCodes)
        val organizedCountryCodes = Array(countryCodes.size) { "" }
        codes = Array(countryCodes.size) { "" }
        for (i in countryCodes.indices) {
            val countryAndCodes = countryCodes[i].split(",")
            val organized = "(${countryAndCodes[1]}) +${countryAndCodes[0]}"
            codes[i] = countryAndCodes[0]
            organizedCountryCodes[i] = organized
        }
        context?.let {
            dataAdapter = ArrayAdapter(it, R.layout.country_spinner_item, organizedCountryCodes)
            dataAdapter.setDropDownViewResource(R.layout.country_spinner_item)
            phone_country_code_spinner.adapter = dataAdapter
        }
        full_name_contact_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    if (text.trim().length > 4)
                        full_name_contact_et.setBackgroundResource(R.drawable.edit_text_green_bg)
                    else
                        full_name_contact_et.setBackgroundResource(R.drawable.edit_text_red_bg)
                    setButtonText()
                }
            }
        })
        phone_country_code_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    setButtonText()
                }
            }
        phone_number_contact_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    if (text.trim().length > 6)
                        phone_number_contact_et.setBackgroundResource(R.drawable.edit_text_green_bg)
                    else
                        phone_number_contact_et.setBackgroundResource(R.drawable.edit_text_red_bg)
                    setButtonText()
                }
            }
        })
        next_contact_btn.setOnClickListener {
            if (full_name_contact_et.text.toString().trim().length > 4 && phone_number_contact_et.text.toString().trim().length > 6) {
                dropViewModel.enableLoading(true)
                next_contact_btn.setBackgroundResource(R.drawable.button_green_bg)
                next_contact_btn.setTextColor(resources.getColor(R.color.white))
                val fullName = full_name_contact_et.text.toString().trim()
                val number = phone_number_contact_et.text.toString().trim()
                val position = phone_country_code_spinner.selectedItemPosition
                val contact = Contact(fullName, PhoneNumber(codes[position].toInt(), number))
                contactViewModel.updateContact(contact)
                dropViewModel.contact = contact
            }
        }
    }

    private fun setObservers() {
        contactViewModel.updateContactLiveData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                dropViewModel.enableLoading(false)
                if (response.content == true)
                    next_contact_btn.findNavController().navigate(R.id.action_contactFragment_to_addressFragment)
                else
                    dropViewModel.handleError(response.errorMessage)
            }
        })
    }

    private fun setButtonText() {
        dropViewModel.savedContact?.let { contact ->
            if (codes[phone_country_code_spinner.selectedItemPosition].toInt() != contact.phoneNumber.countryCode
                || phone_number_contact_et.text.toString() != contact.phoneNumber.number
                || full_name_contact_et.text.toString() != contact.fullName
            ) {
                next_contact_btn.text = getString(R.string.update_button_text)
            } else {
                next_contact_btn.text = getString(R.string.next_button)
            }
        }
    }
}