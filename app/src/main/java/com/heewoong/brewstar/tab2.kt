package com.heewoong.brewstar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class tab2 : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_tab2, container, false)
        recyclerView = root.findViewById(R.id.recycler2)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        val dummy = listOf(
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            topTenDummy("레몬 아샷추", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),

                )
        recyclerView.adapter = topTenAdapter(requireContext(), dummy)
        return root



//        return inflater.inflate(R.layout.fragment_tab2, container, false)
    }
}