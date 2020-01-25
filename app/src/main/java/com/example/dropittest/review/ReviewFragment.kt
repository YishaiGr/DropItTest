package com.example.dropittest.review

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dropittest.R
import com.example.dropittest.databinding.ReviewFragmentBinding
import com.example.dropittest.main.DropActivity
import com.example.dropittest.main.DropViewModel

class ReviewFragment : Fragment() {

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
        val binding: ReviewFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.review_fragment, container, false)
        binding.dropViewModel = dropViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dropViewModel.setTitle(getString(R.string.review_drop_title))
    }
}