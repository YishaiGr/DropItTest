package com.example.dropittest.bags

import android.content.ClipData
import android.content.ClipDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dropittest.R
import kotlinx.android.synthetic.main.bag_item.view.*

class BagAdapter(var list: MutableList<String>, private val bagsViewModel: BagsViewModel): RecyclerView.Adapter<BagAdapter.BagViewHolder>() {

    var isEnabled = true

    fun setBagList(list: MutableList<String>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bag_item, parent, false)
        return BagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        val bagName = list[position]
        holder.name.text = bagName
    }

    inner class BagViewHolder(item: View): RecyclerView.ViewHolder(item){
        val name: TextView = item.bag_name_tv

        init {
            item.setOnLongClickListener {
                if (!isEnabled)
                    return@setOnLongClickListener false
                bagsViewModel.bagView = item
                bagsViewModel.deleteIndex = adapterPosition
                item.visibility = View.INVISIBLE
                val bagName = list[adapterPosition]
                val itemClip = ClipData.Item(bagName)
                val dragData = ClipData(bagName, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), itemClip)
                val shadow = View.DragShadowBuilder(it)
                it.startDrag(dragData, shadow, null, 0)
                true
            }
        }
    }
}