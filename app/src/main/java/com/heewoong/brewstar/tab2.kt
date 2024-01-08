package com.heewoong.brewstar

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityCustomDescriptionBinding
import com.heewoong.brewstar.databinding.ActivityMyCustomAddBinding
import com.heewoong.brewstar.databinding.FragmentTab2Binding


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

        root.findViewById<ImageButton>(R.id.coffeeBtn).setOnClickListener{

            val intent = Intent(requireContext(), ViewCustoms::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

        }




        return root
//        return inflater.inflate(R.layout.fragment_tab2, container, false)
    }



}