package com.heewoong.brewstar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityMainBinding
import com.heewoong.brewstar.databinding.ActivityViewCustomsBinding

class ViewCustoms : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding : ActivityViewCustomsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCustomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recycler2
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val dummy = listOf(
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
            )

        recyclerView.adapter = viewCustomAdapter(this, dummy)

        val categoryList = listOf("Coffee", "NonCoffee", "Frappuccino")
        val categorySpinner: Spinner = binding.categoryDropdown
        val adapter : ArrayAdapter<String> = ArrayAdapter(this@ViewCustoms, android.R.layout.simple_list_item_1, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter =adapter

        val categoryList2 = listOf("Recommend", "Recent")
        val categorySpinner2: Spinner = binding.categoryDropdown2
        val adapter2 : ArrayAdapter<String> = ArrayAdapter(this@ViewCustoms, android.R.layout.simple_list_item_1, categoryList2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner2.adapter =adapter2

        supportActionBar?.hide()


    }
}