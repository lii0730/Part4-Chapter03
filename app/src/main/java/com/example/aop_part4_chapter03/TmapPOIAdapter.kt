package com.example.aop_part4_chapter03

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skt.Tmap.TMapPOIItem

class TmapPOIAdapter(val onItemClicked : (TMapPOIItem) -> Unit) : ListAdapter<TMapPOIItem, TmapPOIAdapter.ViewHolder>(differ) {

    private lateinit var titleTextView : TextView
    private lateinit var subtitleTextView : TextView
    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        fun bind(model : TMapPOIItem) {
            titleTextView = itemView.findViewById(R.id.titleTextView)
            subtitleTextView = itemView.findViewById(R.id.subtitleTextView)

            titleTextView.text = model.poiName
            subtitleTextView.text = "${model.upperAddrName} ${model.middleAddrName} ${model.lowerAddrName}"

            itemView.setOnClickListener {
                onItemClicked(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val differ = object : DiffUtil.ItemCallback<TMapPOIItem>() {
            override fun areItemsTheSame(oldItem: TMapPOIItem, newItem: TMapPOIItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TMapPOIItem, newItem: TMapPOIItem): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}