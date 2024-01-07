package com.heewoong.brewstar

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityMyCustomAddBinding
import com.heewoong.brewstar.databinding.FragmentTab1Binding

class tab1 : Fragment() {

    private lateinit var binding: FragmentTab1Binding
    private lateinit var myCustomAdapter: MyCustomAdapter
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var collectAdapter: FavoriteAdapter
    private lateinit var rv_favorite: RecyclerView
    private lateinit var rv_collect: RecyclerView
    private lateinit var rv_mycustom: RecyclerView
    //private lateinit var btn_edit: Button
    private lateinit var btn_add: ImageButton
    private lateinit var toggle: ToggleButton
    private lateinit var btn_back: ImageButton

    private var myCustomItemList = ArrayList<MyCustomsItem>()
    private var favoriteItemList = ArrayList<FavoriteItem>()
    private var collectItemList = ArrayList<FavoriteItem>()

    // 토글 켤 때 다 invisible하기 위해서
    private lateinit var rectangle1: ImageView
    private lateinit var rectangle2: ImageView
    private lateinit var rectangle3: ImageView
    private lateinit var menu1: ImageButton
    private lateinit var menu2: ImageButton
    private lateinit var menu3: ImageButton
    private lateinit var text1: Button
    private lateinit var text2: Button
    private lateinit var text3: Button

    //popup창을 위한 준비들
    private lateinit var bindingPopup: ActivityMyCustomAddBinding

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

        rectangle1 = binding.favoriteRectangleCoffee
        rectangle2 = binding.favoriteRectangleNoncoffee
        rectangle3 = binding.favoriteRectangleFrappuccino
        menu1 = binding.ivFavoriteMenuCoffee
        menu2 = binding.ivFavoriteMenuNoncoffee
        menu3 = binding.ivFavoriteMenuFrappuccino
        text1 = binding.tvFavoriteMenuCoffee
        text2 = binding.tvFavoriteMenuNoncoffee
        text3 = binding.tvFavoriteMenuFrappuccino
        btn_back = binding.btnBack

        collectAdapter = FavoriteAdapter(collectItemList)
        rv_collect = binding.rvCollectOne
        // 가로로 스크롤 하도록
        rv_collect.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_collect.adapter = collectAdapter

        for (i: Int in 1..10) {
            collectItemList.add(
                FavoriteItem(
                    "레몬 아샷추 하핫",
                    "샷2 + 레몬시럽1 + 복숭아티백 + 헤헤 + 호호"
                )
            )
        }
        
        // favorite Coffee 눌렀을 때
        menu1.setOnClickListener {
            menu1.setImageResource(R.drawable.coffee)
            text1.setText("Coffee")
            clickCollectOne()
        }
        text1.setOnClickListener {
            menu1.setImageResource(R.drawable.coffee)
            text1.setText("Coffee")
            clickCollectOne()
        }
        
        // favorite Non Coffee 눌렀을 때
        menu2.setOnClickListener {
            menu1.setImageResource(R.drawable.noncoffee)
            text1.setText("Non Coffee")
            clickCollectOne()
        }
        text2.setOnClickListener {
            menu1.setImageResource(R.drawable.noncoffee)
            text1.setText("Non Coffee")
            clickCollectOne()
        }
        
        // favorite Frappuccino 눌렀을 때
        menu3.setOnClickListener {
            menu1.setImageResource(R.drawable.frappuccino)
            text1.setText("Frappuccino")
            clickCollectOne()
        }
        text3.setOnClickListener {
            menu1.setImageResource(R.drawable.frappuccino)
            text1.setText("Frappuccino")
            clickCollectOne()
        }

        // 다시 back버튼 누르면
        btn_back.setOnClickListener {
            clickBack()
        }

        
        return binding.root
    }


    private fun clickCollectOne() {
        menu1.isEnabled = false
        text1.isEnabled = false
        rectangle2.visibility = INVISIBLE
        rectangle3.visibility = INVISIBLE
        menu2.visibility = INVISIBLE
        menu3.visibility = INVISIBLE
        text2.visibility = INVISIBLE
        text3.visibility = INVISIBLE

        rv_collect.visibility = VISIBLE
        btn_back.visibility = VISIBLE
    }

    private fun clickBack() {
        menu1.isEnabled = true
        text1.isEnabled = true
        btn_back.visibility = INVISIBLE
        menu1.setImageResource(R.drawable.coffee)
        text1.setText("Coffee")

        rectangle1.visibility = VISIBLE
        rectangle2.visibility = VISIBLE
        rectangle3.visibility = VISIBLE
        menu1.visibility = VISIBLE
        menu2.visibility = VISIBLE
        menu3.visibility = VISIBLE
        text1.visibility = VISIBLE
        text2.visibility = VISIBLE
        text3.visibility = VISIBLE

        rv_collect.visibility = INVISIBLE
    }


    private fun turnToggle() {
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
                menu1.setImageResource(R.drawable.coffee)
                text1.setText("Coffee")
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
                rv_collect.visibility = INVISIBLE
                btn_back.visibility = INVISIBLE
            } else {
                // 토글이 꺼질 때
                menu1.setImageResource(R.drawable.coffee)
                text1.setText("Coffee")
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
                rv_collect.visibility = INVISIBLE
                btn_back.visibility = INVISIBLE
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
            showCustomDialog()
//            val intent = Intent(activity, MyCustomAdd::class.java)
//            startActivityForResult(intent, PHONE_ADD_REQUEST_CODE)
        }
    }

    private fun showCustomDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        bindingPopup = ActivityMyCustomAddBinding.inflate(layoutInflater)
        val view: View = bindingPopup.layoutPopup

        builder.setView(view)

        val alertDialog: AlertDialog = builder.create()

        // close 버튼 누르면 다시 돌아가기
        view.findViewById<ImageButton>(R.id.popupCloseBtn).setOnClickListener {
            alertDialog.dismiss()
        }

        var newName: String = ""
        var newMenu: String = ""
        var newCustom: String = ""
        var newLikes: String = "0"

        // save 버튼 누르면 추가되기
        view.findViewById<Button>(R.id.popupSaveBtn).setOnClickListener {
            newName += view.findViewById<EditText>(R.id.editPopupName).text.toString()
            newMenu += view.findViewById<EditText>(R.id.editPopupMenu).text.toString()
            newCustom += view.findViewById<EditText>(R.id.editPopupCustom).text.toString()

            alertDialog.dismiss()

            myCustomItemList.add(MyCustomsItem(newName, newMenu, newCustom, newLikes))
            myCustomAdapter.notifyDataSetChanged()
        }

        // 다이얼로그 형태 지우기
        if(alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }


    companion object {
        private const val PHONE_ADD_REQUEST_CODE = 1
    }

}