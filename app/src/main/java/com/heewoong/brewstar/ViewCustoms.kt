package com.heewoong.brewstar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.heewoong.brewstar.databinding.ActivityViewCustomsBinding

class ViewCustoms : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding : ActivityViewCustomsBinding
    private lateinit var editText: EditText
    private var searchList = ArrayList<customDummy>()
    private lateinit var searchAdapter: viewCustomAdapter
    private var itemList = ArrayList<customDummy>()

    private lateinit var categorySpinner: Spinner
    private lateinit var categorySpinner2: Spinner
    val categoryList = listOf("All", "Coffee", "Non Coffee", "Frappuccino")
    val categoryList2 = listOf("Recommend", "Recent")
    private var selectedList = ArrayList<customDummy>()
    var selectPosition: Int = 0

    // 스와이프 새로고침
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCustomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefreshLayout = binding.swipeLayout
        swipeRefreshLayout.setOnRefreshListener(this)

        doingMain()
    }

    private fun doingMain() {
        recyclerView = binding.recycler2
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

//        val dummy = listOf(
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", 50),
//            )
        for ( i: Int in 1..10) {
            itemList.add(customDummy("레몬 아샷추","Iced Caffe Latte", "~~~~~~~~~~~~~~~~~~", "heewoong_ahn", "50"))
        }

//        searchAdapter = viewCustomAdapter(this, dummy)
        searchAdapter = viewCustomAdapter(this, itemList)
        recyclerView.adapter = searchAdapter


        // toggle 부분
        //val categoryList = listOf("All", "Coffee", "NonCoffee", "Frappuccino")
        categorySpinner = binding.categoryDropdown
        val adapter : ArrayAdapter<String> = ArrayAdapter(this@ViewCustoms, android.R.layout.simple_list_item_1, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        //val categoryList2 = listOf("Recommend", "Recent")
        categorySpinner2 = binding.categoryDropdown2
        val adapter2 : ArrayAdapter<String> = ArrayAdapter(this@ViewCustoms, android.R.layout.simple_list_item_1, categoryList2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner2.adapter = adapter2

        supportActionBar?.hide()

        // category 중 coffee/noncoffee/frappuccino를 선택했을 때
        val whatSelected: String = intent.getStringExtra("selected").toString()
        Log.d("whatSelected", whatSelected)
        if (whatSelected == "Coffee") {
            selectPosition = 1
        } else if (whatSelected == "Non Coffee") {
            selectPosition = 2
        } else if (whatSelected == "Frappuccino") {
            selectPosition = 3
        } else {
            selectPosition = 0
        }
        categorySpinner.setSelection(selectPosition)
        chooseCategory()

        // search하는 부분
        search()
    }

    private fun search() {
        editText = binding.editSearch
        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val searchText = editText.text.toString()
                searchList.clear()

                if(searchText.isEmpty()) {
                    searchAdapter.setItems(itemList)
                } else {
                    for (a in itemList.indices) {
                        // 나중에는 모든 name, menu..등등에 따라서 다 해야함
                        if (itemList[a].name.toLowerCase().contains(searchText.toLowerCase())) {
                            searchList.add(itemList[a])
                        } else if (itemList[a].menu.toLowerCase().contains(searchText.toLowerCase())) {
                            searchList.add(itemList[a])
                        }
                    }
                    searchAdapter.setItems(searchList)
                }

            }
        })
    }

    private fun chooseCategory() {
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedList.clear()
                val selectedCategory = categoryList[position]
                if (selectedCategory == "Coffee") {
                    Log.d("selected", "Coffee")
                    Log.d("selected", position.toString())
                    searchAdapter.setItems(searchList)
                } else if (selectedCategory == "Non Coffee") {
                    Log.d("selected", "Non Coffee")
                    searchAdapter.setItems(searchList)
                } else if (selectedCategory == "Frappuccino") {
                    Log.d("selected", "Frappuccino")
                    searchAdapter.setItems(searchList)
                } else if (selectedCategory == "All") {
                    Log.d("selected", "All")
                    searchAdapter.setItems(itemList)
                }
                selectPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedList.clear()
                searchAdapter.setItems(selectedList)
            }
        }
    }

    override fun onRefresh() {
        searchList.clear()
        itemList.clear()
        doingMain()

        categorySpinner.setSelection(selectPosition)
        swipeRefreshLayout.isRefreshing = false
    }

}