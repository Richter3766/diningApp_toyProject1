package com.example.test.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.DiningItemBinding
import com.example.test.databinding.ItemLoadingBinding
import com.example.test.model.DiningData


class DiningAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val items = ArrayList<DiningData>()

    // 아이템뷰에 게시물이 들어가는 경우
    inner class ItemViewHolder(private val binding: DiningItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: DiningData){
            binding.nameText.text = data.name
            binding.fieldText.text = data.field
            binding.addressText.text = data.address
        }
    }

    // 아이템뷰에 프로그레스바가 들어가는 경우
    inner class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        // 게시물과 프로그레스바 아이템뷰를 구분할 기준이 필요하다.
        return when (items[position].name) {
            " " -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DiningItemBinding.inflate(layoutInflater, parent, false)
                ItemViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            holder.bind(items[position])
        }else{

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(notice: ArrayList<DiningData>) {
        items.clear()
        items.addAll(notice)
        items.add(DiningData(-1," ", " ", " ")) // progress bar 넣을 자리
        notifyDataSetChanged()
    }

    fun deleteLoading(){
        items.removeAt(items.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    fun getList(): ArrayList<DiningData> {
        return items
    }

    fun filter(query: String): ArrayList<DiningData> {
        val filteredList = ArrayList<DiningData>()

        for (item in items) { // originalList는 모든 데이터를 보유한 리스트로 설정해야 합니다.
            if (item.name.contains(query, true)) { // 검색어를 포함한 데이터만 필터링
                filteredList.add(item)
            }
        }

        return filteredList
    }

}