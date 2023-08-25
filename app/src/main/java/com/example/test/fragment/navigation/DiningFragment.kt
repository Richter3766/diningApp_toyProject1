package com.example.test.fragment.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.helper.toyClient
import com.example.test.adapter.DiningAdapter
import com.example.test.adapter.SearchAdapter
import com.example.test.databinding.FragmentDiningBinding
import com.example.test.model.DiningData
import com.example.test.services.DBService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiningFragment : Fragment() {
    private lateinit var binding:FragmentDiningBinding
    private lateinit var diningAdapter: DiningAdapter
    private lateinit var searchAdapter: SearchAdapter
    private var page = 1
    private var isFetchingData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiningBinding.inflate(layoutInflater)

        val searchViewText = binding.searchViewText
        searchViewText.setOnQueryTextListener(searchViewTextListener)

        diningAdapter = DiningAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isFetchingData){
            fetchData(page)
        }
    }
    private fun fetchData(page: Int) {
        isFetchingData = true

        val retrofit = toyClient.getInstance()
        val myAPI: DBService = retrofit.create(DBService::class.java)
        val TAG = DiningFragment::class.java.simpleName

        myAPI.request(page).enqueue(object : Callback<ArrayList<DiningData>> {
            override fun onResponse(
                call: Call<ArrayList<DiningData>>,
                response: Response<ArrayList<DiningData>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { newData ->
                        diningAdapter.apply {
                            if (page == 1) {
                                setList(newData)
//                                filter(binding.searchViewText.query.toString())
                                applyRecyclerView(getList())

                            } else {
                                val curList = getList()
                                curList.addAll(newData)
                                setList(curList)
                            }
                        }

                    }
                }else{
                    Log.d(TAG, "Response error: ${response.code()}")
                }
                isFetchingData = false
            }

            override fun onFailure(call: Call<ArrayList<DiningData>>, t: Throwable) {
                isFetchingData = false
                Log.d(TAG, t.toString())
            }
        })
    }

    private fun applyRecyclerView(filteredList:ArrayList<DiningData>){
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            searchAdapter = SearchAdapter(filteredList, requireActivity())
            adapter = searchAdapter
        }
    }

    companion object{
        const val TAG = "DiningFragment"

    }

    private val searchViewTextListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            //텍스트 입력/수정시에 호출
            override fun onQueryTextChange(newText: String): Boolean {
                newText?.let {searchQuery ->
                    val filteredList = diningAdapter.filter(searchQuery)
                    applyRecyclerView(filteredList)
                    Log.d(TAG, "SearchView Text is changed: $searchQuery")
                }
                return true
            }
        }
}