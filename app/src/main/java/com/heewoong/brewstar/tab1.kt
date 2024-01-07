package com.heewoong.brewstar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.FragmentTab1Binding

class tab1 : Fragment() {

    private lateinit var binding: FragmentTab1Binding
    private lateinit var myCustomAdapter: MyCustomAdapter
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var rv_favorite: RecyclerView
    private lateinit var rv_mycustom: RecyclerView
    //private lateinit var btn_edit: Button
    private lateinit var btn_add: ImageButton
    private lateinit var toggle: ToggleButton

    private var myCustomItemList = ArrayList<MyCustomsItem>()
    private var favoriteItemList = ArrayList<FavoriteItem>()

    // 토글 켤 때 다 invisible하기 위해서
    private lateinit var rectangle1: ImageView
    private lateinit var rectangle2: ImageView
    private lateinit var rectangle3: ImageView
    private lateinit var menu1: ImageView
    private lateinit var menu2: ImageView
    private lateinit var menu3: ImageView
    private lateinit var text1: TextView
    private lateinit var text2: TextView
    private lateinit var text3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTab1Binding.inflate(inflater, container, false)
        turnToggle()
        getAllMyCustom()

        return binding.root
    }


    private fun turnToggle() {
        rectangle1 = binding.favoriteRectangleCoffee
        rectangle2 = binding.favoriteRectangleNoncoffee
        rectangle3 = binding.favoriteRectangleFrappuccino
        menu1 = binding.ivFavoriteMenuCoffee
        menu2 = binding.ivFavoriteMenuNoncoffee
        menu3 = binding.ivFavoriteMenuFrappuccino
        text1 = binding.tvFavoriteMenuCoffee
        text2 = binding.tvFavoriteMenuNoncoffee
        text3 = binding.tvFavoriteMenuFrappuccino

        rv_favorite = binding.rvFavorite


        favoriteAdapter = FavoriteAdapter(favoriteItemList)
        rv_favorite = binding.rvFavorite
        // 가로로 스크롤 하도록
        rv_favorite.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_favorite.adapter = favoriteAdapter

        for (i: Int in 1..10) {
            favoriteItemList.add(
                FavoriteItem(
                    "레몬 아샷추 하핫",
                    "샷2 + 레몬시럽1 + 복숭아티백 + 헤헤 + 호호"
                )
            )
        }



        toggle = binding.toggle
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 토글이 켜질 때
                rectangle1.visibility = INVISIBLE
                rectangle2.visibility = INVISIBLE
                rectangle3.visibility = INVISIBLE
                menu1.visibility = INVISIBLE
                menu2.visibility = INVISIBLE
                menu3.visibility = INVISIBLE
                text1.visibility = INVISIBLE
                text2.visibility = INVISIBLE
                text3.visibility = INVISIBLE

                rv_favorite.visibility = VISIBLE
            } else {
                // 토글이 꺼질 때
                rectangle1.visibility = VISIBLE
                rectangle2.visibility = VISIBLE
                rectangle3.visibility = VISIBLE
                menu1.visibility = VISIBLE
                menu2.visibility = VISIBLE
                menu3.visibility = VISIBLE
                text1.visibility = VISIBLE
                text2.visibility = VISIBLE
                text3.visibility = VISIBLE

                rv_favorite.visibility = INVISIBLE
            }
        }
    }

    private fun getAllMyCustom() {
        myCustomAdapter = MyCustomAdapter(myCustomItemList)
        rv_mycustom = binding.rvMycustom
        rv_mycustom.layoutManager = LinearLayoutManager(requireContext())
        rv_mycustom.adapter = myCustomAdapter

        for (i: Int in 1..10) {
            myCustomItemList.add(
                MyCustomsItem(
                    "레몬 아샷추",
                    "Iced Caffe Americano",
                    "샷2 + 레몬시럽1 + 복숭아티백",
                    "101"
                )
            )
        }

        btn_add = binding.btnAdd
        btn_add.setOnClickListener {
            val intent = Intent(activity, MyCustomAdd::class.java)
            startActivityForResult(intent, PHONE_ADD_REQUEST_CODE)
        }
    }

    companion object {
        private const val PHONE_ADD_REQUEST_CODE = 1
    }

}