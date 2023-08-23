package com.example.test.fragment.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.DBhelper.toyClient
import com.example.test.adapter.diningAdapter
import com.example.test.databinding.FragmentDiningBinding
import com.example.test.model.DiningData
import com.example.test.services.DBService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiningFragment : Fragment() {
    private lateinit var binding:FragmentDiningBinding
    private lateinit var diningAdapter: diningAdapter
    private var page = 1
    private var isFetchingData = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiningBinding.inflate(layoutInflater)

        diningAdapter = diningAdapter()

        applyRecyclerView()

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

    private fun applyRecyclerView(){
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = diningAdapter

//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//                    val totalItemCount = layoutManager.itemCount
//
//                    if (!isFetchingData && lastVisibleItemPosition == totalItemCount - 1) {
//                        // Load more data when reaching the last item
//                        fetchDataRunnable.run()
//                    }
////                    if (!recyclerView.canScrollVertically(1) && !isFetchingData) {
////                        page++
////                        fetchData(page)
////                    }
//                }
//            })
        }
    }
}