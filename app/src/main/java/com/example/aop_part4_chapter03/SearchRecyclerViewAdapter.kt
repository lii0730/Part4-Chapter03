package com.example.aop_part4_chapter03

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aop_part4_chapter03.databinding.RecyclerviewItemBinding
import com.example.aop_part4_chapter03.model.SearchResultEntity

class SearchRecyclerViewAdapter() :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    private var searchResultList: List<SearchResultEntity> = listOf()
    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit


    inner class ViewHolder(
        val binding: RecyclerviewItemBinding,
        val searchResultClickListener: (SearchResultEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SearchResultEntity) = with(binding) {
            titleTextView.text = data.name
            subtitleTextView.text = data.fullAdress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        val binding = RecyclerviewItemBinding.bind(view)
        return ViewHolder(binding, searchResultClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchResultList[position])
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    fun setSearchResultListener(searchResultList: List<SearchResultEntity>, searchResultClickListener: (SearchResultEntity) -> Unit) {
        this.searchResultList =searchResultList
        this.searchResultClickListener = searchResultClickListener
    }
}