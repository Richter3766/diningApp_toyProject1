package com.example.test.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.DiningItemBinding
import com.example.test.model.DiningData

class SearchAdapter(var datas: ArrayList<DiningData>): RecyclerView.Adapter<SearchAdapter.ViewHolder>(), Filterable {
    var filteredDining = ArrayList<DiningData>()
    var itemFilter = ItemFilter()

    inner class ViewHolder(private val binding: DiningItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        var iv_person_phone_book_list_item: ImageView
//        var tv_name_phone_book_list_item: TextView
//        var tv_phone_number_phone_book_list_item: TextView

        fun bind(data: DiningData){
            binding.nameText.text = data.name
            binding.fieldText.text = data.field
            binding.addressText.text = data.address
        }
    }

    init{
        filteredDining.addAll(datas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DiningItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind(filteredDining[position])

    }

    override fun getItemCount(): Int {
        return filteredDining.size
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter: Filter(){
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            val filteredList: ArrayList<DiningData> = ArrayList()
            if(filterString.trim {it <= ' '}.isEmpty()){
                results.values = datas
                results.count = datas.size

                return results
            }else{
                for(data in datas){
                    if(data.name.contains(filterString) || data.address.contains(filterString))
                        filteredList.add(data)
                }
            }
            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence, filteredResults: FilterResults) {
            filteredDining.clear()
            filteredDining.addAll(filteredResults.values as ArrayList<DiningData>)
            notifyDataSetChanged()
        }

    }
}