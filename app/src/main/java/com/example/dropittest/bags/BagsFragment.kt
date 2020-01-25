package com.example.dropittest.bags

import android.content.Context
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dropittest.R
import com.example.dropittest.main.DropActivity
import com.example.dropittest.main.DropViewModel
import kotlinx.android.synthetic.main.bags_fragment.*
import javax.inject.Inject

class BagsFragment : Fragment() {

    @Inject lateinit var bagsViewModel: BagsViewModel
    private lateinit var bagAdapter: BagAdapter
    private lateinit var randomBagAdapter: RandomBagAdapter
    private lateinit var dropViewModel: DropViewModel

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
        return inflater.inflate(R.layout.bags_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dropViewModel.setTitle(getString(R.string.bags_title))
        dropViewModel.enableLoading(false)
        dropViewModel.enableBackButton(true)
        setFields()
        setObservers()
        setGarbageDragDrop()
    }

    private fun setFields(){
        randomBagAdapter = RandomBagAdapter(bagsViewModel.getRandomBagNames(), bagsViewModel)
        random_bags_bags_rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        random_bags_bags_rv.adapter = randomBagAdapter

        bagAdapter = BagAdapter(mutableListOf(), bagsViewModel)
        selected_bags_bags_rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        selected_bags_bags_rv.adapter = bagAdapter

        save_bags_bags_btn.setOnClickListener {
            if(bagAdapter.list.size>0){
                dropViewModel.enableLoading(true)
                enableAdapters(false)
                save_bags_bags_btn.setBackgroundResource(R.drawable.button_green_bg)
                dropViewModel.bags = bagAdapter.list
                dropViewModel.drop()
            }
            else {
                bags_amount_bags_tv.setBackgroundResource(R.drawable.rv_red_bg)
            }
        }
    }

    private fun setObservers(){
        bagsViewModel.selectedBagList.observe(viewLifecycleOwner, Observer { list ->
            if (list.size>0)
                bags_amount_bags_tv.setBackgroundResource(R.drawable.rv_normal_bg)
            bagAdapter.setBagList(list)
            bags_amount_bags_tv.text = getString(R.string.bags_amount, list.size)
        })
        bagsViewModel.randomBagList.observe(viewLifecycleOwner, Observer { list ->
            randomBagAdapter.setBagList(list)
        })
        dropViewModel.dropLiveDataOnce.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {response ->
                dropViewModel.enableLoading(false)
                enableAdapters(true)
                if (response.content==true)
                    save_bags_bags_btn.findNavController().navigate(R.id.action_bagsFragment_to_reviewFragment)
                else
                    dropViewModel.handleError(response.errorMessage)
            }
        })
    }

    private fun setGarbageDragDrop(){
        garbage_bags_view.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.setBackgroundResource(R.drawable.grabage_green_drawable)
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.setBackgroundResource(R.drawable.garbage_black_drawable)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    bagsViewModel.deleteSelectedBag()
                    bagsViewModel.addRandomBag(dragEvent.clipData.getItemAt(0).text.toString())
                    bagsViewModel.bagView?.visibility = View.VISIBLE
                    bagsViewModel.bagView = null
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    bagsViewModel.bagView?.visibility = View.VISIBLE
                    bagsViewModel.bagView = null
                    view.setBackgroundResource(R.drawable.garbage_black_drawable)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun enableAdapters(on: Boolean){
        bagAdapter.isEnabled = on
        randomBagAdapter.isEnabled = on
    }
}