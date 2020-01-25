package com.example.dropittest.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dropittest.R
import kotlinx.android.synthetic.main.review_bag_item.view.*

class ReviewBagAdapter(var list: ArrayList<String>): RecyclerView.Adapter<ReviewBagAdapter.ReviewBagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_bag_item, parent, false)
        return ReviewBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ReviewBagViewHolder, position: Int) {
        val bag = list[position]
        holder.name.text = bag
    }

    inner class ReviewBagViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val name: TextView = item.review_bag_name_tv
    }
}