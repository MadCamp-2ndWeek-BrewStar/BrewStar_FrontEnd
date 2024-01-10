package com.heewoong.brewstar


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.heewoong.brewstar.databinding.FragmentTab2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class tab2 : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentTab2Binding
    private lateinit var recyclerView: RecyclerView
//    private var itemList = ArrayList<topTenDummy>()

    private lateinit var topTenAdapter: topTenAdapter
    private var customItemList = ArrayList<CustomItem>()
    // 스와이프 새로고침
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    val api = RetrofitInterface.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTab2Binding.inflate(inflater, container, false)

        swipeRefreshLayout = binding.swipeLayout
        swipeRefreshLayout.setOnRefreshListener(this)
        doingMain()

        return binding.root
    }

    private fun doingMain() {
        recyclerView = binding.recycler2
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        getTab2()

        topTenAdapter = topTenAdapter(requireContext(), customItemList)


        //recyclerView.adapter = topTenAdapter(requireContext(), dummy)
        recyclerView.adapter = topTenAdapter

        binding.coffeeCard.setOnClickListener{
            // 여기서, @GET("/Coffee) 불러와야.
            val intent = Intent(requireContext(), ViewCustoms::class.java)
            intent.putExtra("selected", "Coffee")
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        binding.nonCoffeeCard.setOnClickListener{
            // 여기서, @GET("/Non-Coffee) 불러와야.
            val intent = Intent(requireContext(), ViewCustoms::class.java)
            intent.putExtra("selected", "Non-Coffee")
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        binding.frappuccinoCard.setOnClickListener{
            // 여기서, @GET("/Frappuccino) 불러와야.
            val intent = Intent(requireContext(), ViewCustoms::class.java)
            intent.putExtra("selected", "Frappuccino")
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    // favorite 누른 애들
    private fun getTab2() {
        // data를 받아서, favoriteItemList에 add하면 됨.
        api.getAllCustoms("Recommend").enqueue(object : Callback<List<List<String>>> {
            override fun onResponse(call: Call<List<List<String>>>, response: Response<List<List<String>>>) {
                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, "네트워크 오류: dd")
                    val result = response.body()
                    result?.let{
                        handleTab2(it)
                    }
                } else {
                    // HTTP 요청이 실패한 경우의 처리
                    Log.e(ContentValues.TAG, "HTTP 요청 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<List<String>>>, t: Throwable) {
                Log.e(ContentValues.TAG, "네트워크 오류: ${t.message}")
            }
        })
    }
    private fun handleTab2(data: List<List<String>>) {
        var cnt: Int  = 0

        for (record in data) {
            val getId = record[0]
            val getcategory = record[1]
            val getname = record[2]
            val getmenu = record[3]
            val getcustom = record[4]
            val getdescription = record[5]
            val getcreator = record[6]
            val getlikes = record[7]
            //time은 backend에서 정렬해서 주기 때문에 받을 필요가 없음.


            val customItem = CustomItem(getId, getcategory, getname, getmenu, getcustom, getdescription, getcreator, getlikes)
            customItemList.add(customItem)
            topTenAdapter.notifyDataSetChanged()

            cnt++

            if (cnt>=10){
                break;
            }
        }
    }


    override fun onRefresh() {
        customItemList.clear()
        doingMain()
        swipeRefreshLayout.isRefreshing = false
    }


}