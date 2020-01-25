package com.example.dropittest.util

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dropittest.R
import com.example.dropittest.databinding.ErrorFragmentBinding
import com.example.dropittest.main.DropActivity
import com.example.dropittest.main.DropViewModel
import kotlinx.android.synthetic.main.error_fragment.*

class ErrorFragment : Fragment() {

    private lateinit var dropViewModel: DropViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            dropViewModel = (it as DropActivity).dropViewModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: ErrorFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.error_fragment, container, false)
        binding.dropViewModel = dropViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dropViewModel.enableBackButton(false)
        setButtons()
    }

    private fun setButtons(){
        refresh_error_button.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}