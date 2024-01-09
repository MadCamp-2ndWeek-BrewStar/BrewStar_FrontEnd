package com.heewoong.brewstar


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.heewoong.brewstar.databinding.FragmentTab2Binding


class tab2 : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentTab2Binding
    private lateinit var recyclerView: RecyclerView
    private var itemList = ArrayList<topTenDummy>()

    // 스와이프 새로고침
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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

//        val dummy = listOf(
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//            topTenDummy("레몬 아샷추", "~~~~~~~~~~~", "heewoong_ahn", 50),
//
//                )

        for ( i: Int in 1..10) {
            itemList.add(topTenDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "50"))
        }

        //recyclerView.adapter = topTenAdapter(requireContext(), dummy)
        recyclerView.adapter = topTenAdapter(requireContext(), itemList)

        binding.coffeeCard.setOnClickListener{
            // 여기서, @GET("/Coffee) 불러와야.
            val intent = Intent(requireContext(), ViewCustoms::class.java)
            intent.putExtra("selected", "Coffee")
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        binding.nonCoffeeCard.setOnClickListener{
            // 여기서, @GET("/Non-Coffee) 불러와야.
            val intent = Intent(requireContext(), ViewCustoms::class.java)
            intent.putExtra("selected", "Non Coffee")
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        binding.frappuccinoCard.setOnClickListener{
            // 여기서, @GET("/Frappuccino) 불러와야.
            val intent = Intent(requireContext(), ViewCustoms::class.java)
            intent.putExtra("selected", "Frappuccino")
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    override fun onRefresh() {
        itemList.clear()
        doingMain()
        swipeRefreshLayout.isRefreshing = false
    }


}