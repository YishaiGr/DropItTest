package com.example.dropittest.bags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dropittest.R
import kotlinx.android.synthetic.main.bag_random_item.view.*

class RandomBagAdapter(var list: MutableList<String>, private val bagsViewModel: BagsViewModel):
    RecyclerView.Adapter<RandomBagAdapter.RandomBagViewHolder>() {

    var isEnabled = true

    fun setBagList(list: MutableList<String>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bag_random_item, parent, false)
        return RandomBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RandomBagViewHolder, position: Int) {
        val bagName = list[position]
        holder.name.text = bagName
    }

    inner class RandomBagViewHolder(item: View): RecyclerView.ViewHolder(item){
        val name: TextView = item.bag_random_name_tv

        init {
            item.add_selected_bag_btn.setOnClickListener {
                if (!isEnabled)
                    return@setOnClickListener
                bagsViewModel.addSelectedBag(list[adapterPosition])
                bagsViewModel.deleteRandomBag(adapterPosition)
            }
        }
    }
}