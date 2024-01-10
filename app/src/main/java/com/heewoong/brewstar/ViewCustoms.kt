package com.heewoong.brewstar

import android.content.ContentValues
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.heewoong.brewstar.databinding.ActivityViewCustomsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ViewCustoms : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding : ActivityViewCustomsBinding
    private lateinit var editText: EditText
    private var searchList = ArrayList<CustomItem>()
    private var customList = ArrayList<CustomItem>()
    private var toggleLists = HashMap<String, ArrayList<CustomItem>>() // Modified line
    private lateinit var searchAdapter: viewCustomAdapter
    private lateinit var categorySpinner: Spinner
    private lateinit var categorySpinner2: Spinner
    val categoryList = listOf("All", "Coffee", "Non-Coffee", "Frappuccino")
    val categoryList2 = listOf("Recommend", "Recent")
    private var selectedList = ArrayList<CustomItem>()
    var selectPosition: Int = 0
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    val api = RetrofitInterface.create()
    private lateinit var selectedCategory : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCustomsBinding.inflate(layoutInflater)
        doingMain()

        lifecycleScope.launch {
            delay(300)
            setContentView(binding.root)
        }

        swipeRefreshLayout = binding.swipeLayout
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun doingMain() {
        recyclerView = binding.recycler2
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        getTab2()

        searchAdapter = viewCustomAdapter(this, customList)
        recyclerView.adapter = searchAdapter

        categorySpinner = binding.categoryDropdown
        val adapter : ArrayAdapter<String> = ArrayAdapter(this@ViewCustoms, android.R.layout.simple_list_item_1, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        categorySpinner2 = binding.categoryDropdown2
        val adapter2 : ArrayAdapter<String> = ArrayAdapter(this@ViewCustoms, android.R.layout.simple_list_item_1, categoryList2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner2.adapter = adapter2

        supportActionBar?.hide()

        val whatSelected: String = intent.getStringExtra("selected").toString()
        Log.d("whatSelected", whatSelected)
        if (whatSelected == "Coffee") {
            selectPosition = 1
        } else if (whatSelected == "Non-Coffee") {
            selectPosition = 2
        } else if (whatSelected == "Frappuccino") {
            selectPosition = 3
        } else {
            selectPosition = 0
        }
        categorySpinner.setSelection(selectPosition)

        chooseCategory()
        search()
    }

    private fun getTab2() {
        api.getAllCustoms("Recommend").enqueue(object : Callback<List<List<String>>> {
            override fun onResponse(call: Call<List<List<String>>>, response: Response<List<List<String>>>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result?.let{
                        handleTab2(it)
                    }
                } else {
                    Log.e(ContentValues.TAG, "HTTP 요청 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<List<String>>>, t: Throwable) {
                Log.e(ContentValues.TAG, "네트워크 오류: ${t.message}")
            }
        })
    }

    private fun handleTab2(data: List<List<String>>) {
        for (record in data) {
            val getId = record[0]
            val getcategory = record[1]
            val getname = record[2]
            val getmenu = record[3]
            val getcustom = record[4]
            val getdescription = record[5]
            val getcreator = record[6]
            val getlikes = record[7]

            val customItem = CustomItem(getId, getcategory, getname, getmenu, getcustom, getdescription, getcreator, getlikes)
            customList.add(customItem)
            searchList.add(customItem)

            toggleLists.getOrPut(getcategory) { ArrayList() }.add(customItem) // Modified line
        }
    }

    private fun search() {
        editText = binding.editSearch
        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val searchText = editText.text.toString()
                searchList.clear()

                if (searchText.isEmpty() && selectedCategory == "All") {
                    searchAdapter.setItems(customList)
                } else {
                    val toggleList = toggleLists[selectedCategory]
                    toggleList?.let {
                        for (a in it.indices) {
                            if (it[a].name.toLowerCase(Locale.getDefault()).contains(searchText.toLowerCase(Locale.getDefault()))) {
                                searchList.add(it[a])
                            } else if (it[a].menu.toLowerCase(Locale.getDefault()).contains(searchText.toLowerCase(Locale.getDefault()))) {
                                searchList.add(it[a])
                            }
                        }
                        searchAdapter.setItems(searchList)
                    }
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
                selectedCategory = categoryList[position]
                if (selectedCategory == "All") {
                    searchAdapter.setItems(customList)
                } else {
                    val toggleList = toggleLists[selectedCategory]
                    toggleList?.let {
                        searchAdapter.setItems(it)
                    }
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
        customList.clear()
        toggleLists.values.forEach { it.clear() }
        doingMain()

        categorySpinner.setSelection(selectPosition)
        swipeRefreshLayout.isRefreshing = false
    }
}