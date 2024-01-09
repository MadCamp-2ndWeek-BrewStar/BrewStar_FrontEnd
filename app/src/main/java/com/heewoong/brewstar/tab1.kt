package com.heewoong.brewstar

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.heewoong.brewstar.databinding.ActivityMyCustomAddBinding
import com.heewoong.brewstar.databinding.FragmentTab1Binding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class tab1 : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentTab1Binding
    private lateinit var myCustomAdapter: MyCustomAdapter
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var collectAdapterCoffee: FavoriteAdapter
    private lateinit var collectAdapterNonCoffee: FavoriteAdapter
    private lateinit var collectAdapterFrappuccino: FavoriteAdapter

    private lateinit var rv_favorite: RecyclerView
    private lateinit var rv_collect_coffee: RecyclerView
    private lateinit var rv_collect_non_coffee: RecyclerView
    private lateinit var rv_collect_frappuccino: RecyclerView
    private lateinit var rv_mycustom: RecyclerView
    //private lateinit var btn_edit: Button
    private lateinit var btn_add: ImageButton
    private lateinit var toggle: ToggleButton
    private lateinit var btn_back: ImageButton

    private var myCustomItemList = ArrayList<MyCustomsItem>()
    private var favoriteItemList = ArrayList<MyCustomsItem>()
    private var collectItemListCoffee = ArrayList<MyCustomsItem>()
    private var collectItemListNonCoffee = ArrayList<MyCustomsItem>()
    private var collectItemListFrappuccino = ArrayList<MyCustomsItem>()

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

    //popup창을 위한 준비들
    private lateinit var bindingPopup: ActivityMyCustomAddBinding

    // 서버에서 불러오기
    val api = RetrofitInterface.create()

    // 스와이프해서 새로고침 구현
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentTab1Binding.inflate(inflater, container, false)

        // 새로고침
        swipeRefreshLayout = binding.swipeLayout
        swipeRefreshLayout.setOnRefreshListener(this)

        initiation() // 모든 view들 정의
        turnToggle() // Favorite Customs 구현 부분
        getAllMyCustom() // My Customs 구현 부분
//        clickCategory() // 각 카테고리 눌렀을 때 구현 부분
        lifecycleScope.launch {
            delay(1000)
            clickCategory() // 각 카테고리 눌렀을 때 구현 부분
        }

        
        return binding.root
    }

    
    private fun initiation() {
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
    }

    private fun clickCoffee() {
        // 여기서,@GET("/favorite") 불러 온 뒤, 그 데이터를 가지고
        // @GET("/Coffee")를 불러와야.

        rectangle1.setOnClickListener {
            menu1.setImageResource(R.drawable.coffee)
            text1.setText("Coffee")
            clickCollectOne()
            rv_collect_coffee.visibility = VISIBLE
        }
    }
    private fun clickNonCoffee() {
        // 여기서,@GET("/favorite") 불러 온 뒤, 그 데이터를 가지고
        // @GET("/Non-Coffee")를 불러와야.

        rectangle2.setOnClickListener {
            menu1.setImageResource(R.drawable.noncoffee)
            text1.setText("Non Coffee")
            clickCollectOne()
            rv_collect_non_coffee.visibility = VISIBLE
        }
    }
    private fun clickFrappuccino() {
        // 여기서,@GET("/favorite") 불러 온 뒤, 그 데이터를 가지고
        // @GET("/Frappuccino")를 불러와야.

        rectangle3.setOnClickListener {
            menu1.setImageResource(R.drawable.frappuccino)
            text1.setText("Frappuccino")
            clickCollectOne()
            rv_collect_frappuccino.visibility = VISIBLE
        }
    }

    private fun clickCollectOne() {
        rectangle1.isEnabled = false
        rectangle2.isEnabled = false
        rectangle3.isEnabled = false
        rectangle2.visibility = INVISIBLE
        rectangle3.visibility = INVISIBLE
        menu2.visibility = INVISIBLE
        menu3.visibility = INVISIBLE
        text2.visibility = INVISIBLE
        text3.visibility = INVISIBLE

        rv_collect_coffee.visibility = INVISIBLE
        rv_collect_non_coffee.visibility = INVISIBLE
        rv_collect_frappuccino.visibility = INVISIBLE
        btn_back.visibility = VISIBLE
    }

    private fun clickBack() {
        rectangle1.isEnabled = true
        rectangle2.isEnabled = true
        rectangle3.isEnabled = true
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

        rv_collect_coffee.visibility = INVISIBLE
        rv_collect_non_coffee.visibility = INVISIBLE
        rv_collect_frappuccino.visibility = INVISIBLE
    }


    private fun turnToggle() {
        
        // recyclerview 정의 및 adapter 연결
        rv_favorite = binding.rvFavorite
        favoriteAdapter = FavoriteAdapter(favoriteItemList)
        rv_favorite = binding.rvFavorite
        // 가로로 스크롤 하도록
        rv_favorite.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_favorite.adapter = favoriteAdapter

//        // dummy data
//        for (i: Int in 1..10) {
//            favoriteItemList.add(
//                FavoriteItem(
//                    "레몬 아샷추 하핫",
//                    "샷2 + 레몬시럽1 + 복숭아티백 + 헤헤 + 호호",
//                    "Coffee"
//                )
//            )
//        }

        // 여기서, @GET("/favorites")를 불러와야.
        getTab1Favorite()

        toggle = binding.toggle
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 토글이 켜질 때
                menu1.setImageResource(R.drawable.coffee)
                text1.setText("Coffee")
                rectangle1.isEnabled = false
                rectangle2.isEnabled = false
                rectangle3.isEnabled = false
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
                rv_collect_coffee.visibility = INVISIBLE
                rv_collect_non_coffee.visibility = INVISIBLE
                rv_collect_frappuccino.visibility = INVISIBLE
                btn_back.visibility = INVISIBLE
            } else {
                // 토글이 꺼질 때
                menu1.setImageResource(R.drawable.coffee)
                text1.setText("Coffee")
                rectangle1.isEnabled = true
                rectangle2.isEnabled = true
                rectangle3.isEnabled = true
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
                rv_collect_coffee.visibility = INVISIBLE
                rv_collect_non_coffee.visibility = INVISIBLE
                rv_collect_frappuccino.visibility = INVISIBLE
                btn_back.visibility = INVISIBLE
            }
        }
    }

    private fun clickCategory() {
        // adapter와 연결
        collectAdapterCoffee = FavoriteAdapter(collectItemListCoffee)
        collectAdapterNonCoffee = FavoriteAdapter(collectItemListNonCoffee)
        collectAdapterFrappuccino = FavoriteAdapter(collectItemListFrappuccino)
        rv_collect_coffee = binding.rvCollectCoffee
        rv_collect_non_coffee = binding.rvCollectNonCoffee
        rv_collect_frappuccino = binding.rvCollectFrappuccino

        // 가로로 스크롤 하도록
        rv_collect_coffee.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_collect_coffee.adapter = collectAdapterCoffee
        rv_collect_non_coffee.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_collect_non_coffee.adapter = collectAdapterNonCoffee
        rv_collect_frappuccino.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_collect_frappuccino.adapter = collectAdapterFrappuccino

//        // dummy data
//        for (i: Int in 1..10) {
//            collectItemList.add(
//                FavoriteItem(
//                    "레몬 아샷추 하핫",
//                    "샷2 + 레몬시럽1 + 복숭아티백 + 헤헤 + 호호",
//                    "Coffee"
//                )
//            )
//        }

        // favoriteItemList에서 골라서 채우기
        Log.d("ohohohohoh", "이건 안 뜨냐?: $favoriteItemList")
        for (item in favoriteItemList) {
            if (item.category == "Coffee") {
                Log.d(ContentValues.TAG, "Added items to collectItemListCoffee: $item")
                collectItemListCoffee.add(item)
                collectAdapterCoffee.notifyDataSetChanged()
            } else if (item.category == "Non-Coffee") {
                collectItemListNonCoffee.add(item)
                collectAdapterNonCoffee.notifyDataSetChanged()
            } else if (item.category == "Frappuccino") {
                collectItemListFrappuccino.add(item)
                collectAdapterFrappuccino.notifyDataSetChanged()
            }
        }

        // favorite Coffee 눌렀을 때
        clickCoffee()
        // favorite Non Coffee 눌렀을 때
        clickNonCoffee()
        // favorite Frappuccino 눌렀을 때
        clickFrappuccino()

        // 다시 back버튼 누르면
        btn_back.setOnClickListener {
            clickBack()
        }
    }



    private fun getAllMyCustom() {
        myCustomAdapter = MyCustomAdapter(myCustomItemList)
        rv_mycustom = binding.rvMycustom
        rv_mycustom.layoutManager = LinearLayoutManager(requireContext())
        rv_mycustom.adapter = myCustomAdapter

//        for (i: Int in 1..10) {
//            myCustomItemList.add(
//                MyCustomsItem(
//                    "레몬 아샷추",
//                    "Iced Caffe Americano",
//                    "샷2 + 레몬시럽1 + 복숭아티백",
//                    "101"
//                )
//            )
//        }

        // 여기서,@GET("/mycustoms") 불러와야.
        getTab1MyCustoms()

        btn_add = binding.btnAdd
        btn_add.setOnClickListener {
            showCustomDialog()
//            val intent = Intent(activity, MyCustomAdd::class.java)
//            startActivityForResult(intent, PHONE_ADD_REQUEST_CODE)
        }

        itemTouch()

    }

    private fun showCustomDialog() {
        
        // 이건 add 했을 때에 뜨는 창
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
        var newCategory: String = "Coffee"
        var newDescription: String = ""
        var newCreator: String = "안희웅"
        var newCreatornum: String = "3259657340"

        // save 버튼 누르면 추가되기
        view.findViewById<Button>(R.id.popupSaveBtn).setOnClickListener {
            newName += view.findViewById<EditText>(R.id.editPopupName).text.toString()
            newMenu += view.findViewById<EditText>(R.id.editPopupMenu).text.toString()
            newCustom += view.findViewById<EditText>(R.id.editPopupCustom).text.toString()
            newDescription += view.findViewById<EditText>(R.id.editPopupDescription).text.toString()

            if (newMenu == "Java Chip Frappuccino" ||
                newMenu == "Caramel Frappuccino" ||
                newMenu == "Malcha Cream Frappuccino from Jeju Organiic Farm") {
                newCategory = "Frappuccino"
                view.findViewById<ImageView>(R.id.popupCoffee).setImageResource(R.drawable.frappuccino)
            } else if (newMenu == "Iced Grapefruit Honey Black Tea" ||
                newMenu == "Grapefruit Honey Black Tea" ||
                newMenu == "Iced Malcha Latte from Jeju Organic Farm" ||
                newMenu == "Iced Signature Chocolate" ||
                newMenu == "Signature Chocolate") {
                newCategory = "Non-Coffee"
                view.findViewById<ImageView>(R.id.popupCoffee).setImageResource(R.drawable.noncoffee)
            }

            alertDialog.dismiss()

            // 여기서는, @POST("/mycustoms")로 새로 추가된 애들을 불러가야 함.
            // 그리고 각 메뉴에 따라서 사진을 바꿔야 할 것 같은데... 이 조건문도 다 써보긴 해야할 듯.
            // name, menu, custom, description은 입력한 그대로
            // category는 메뉴 보고 조건문 써서 입력
            // creator, creatornum은 이 사람의 정보 그대로. 근데 불러오기도 해야하나?
            // likes와 wish는 모두 0, O로 초기화.
            val call = api.addCustom(newName, newMenu, newCategory, newCustom, newDescription, newCreator, newCreatornum)
            call.enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.e("Lets go", "success!! good!!")
                    } else {
                        Log.e("Lets go", "what's wrong...")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("mad..nn", "so sad plz")
                }
            })
            
            myCustomItemList.add(MyCustomsItem(newName, newMenu, newCustom, newLikes, newCategory, newDescription, newCreator))
            myCustomAdapter.notifyDataSetChanged()
        }

        // 다이얼로그 형태 지우기
        if(alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    // 스와이프해서 삭제
    private fun itemTouch() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos: Int = viewHolder.adapterPosition
                val toPos: Int = target.adapterPosition
                myCustomAdapter.swapData(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myCustomAdapter.removeMyCustomItem(viewHolder.layoutPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                // 배경색 변경을 위한 로직 추가
                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.WHITE)
                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.remove)

                if (dX < 0) {
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background?.draw(c)

                    // 아이콘 크기 설정
                    val iconSizeInDp = 48
                    val iconSizeInPx = (iconSizeInDp * resources.displayMetrics.density).toInt()

                    // 아이콘 위치 설정
                    // icon?.intrinsicHight!! 대신 iconSizeInPx를 넣음
                    // icon을 배경 오른쪽 중앙에 넣음
                    val iconMargin = (itemView.height - iconSizeInPx) / 2
                    val iconTop = itemView.top + (itemView.height - iconSizeInPx) / 2
                    val iconRight = itemView.right - iconMargin
                    val iconLeft = iconRight - iconSizeInPx
                    val iconBottom = iconTop + iconSizeInPx

                    icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    icon?.draw(c)

                } else {
                    background.setBounds(0, 0, 0, 0)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_mycustom)
    }



    // favorite 누른 애들
    private fun getTab1Favorite() {
        // data를 받아서, favoriteItemList에 add하면 됨.
        api.getFavorite("3259657340").enqueue(object : Callback<List<List<String>>> {
            override fun onResponse(call: Call<List<List<String>>>, response: Response<List<List<String>>>) {
                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, "네트워크 오류: dd")
                    val result = response.body()
                    result?.let{
                        handleTab1Favorite(it)
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
    private fun handleTab1Favorite(data: List<List<String>>) {
        for (record in data) {
            val getname = record[2]
            val getmenu = record[3]
            val getcustom = record[4]
            val getlikes = record[7]
            val getcategory = record[1]
            val getdescription = record[5]
            val getcreator = record[6]
            Log.d("plz name", "$getname")

//            // favoriteItem 형식으로 변환
//            val favoriteItem = FavoriteItem(getname, getcustom, getcategory)
            val favoriteItem = MyCustomsItem(getname, getmenu, getcustom, getlikes, getcategory, getdescription, getcreator)
            Log.d(ContentValues.TAG, "Added items to collectItemList: $favoriteItem")
            favoriteItemList.add(favoriteItem)
            Log.d(ContentValues.TAG, "Added items to collectItemList: $favoriteItemList")
            favoriteAdapter.notifyDataSetChanged()
        }
    }


    // My Customs 받기
    private fun getTab1MyCustoms() {
        // data를 받아서, myCustomItemList에 add하면 됨.
        api.getMyCustom("3259657340").enqueue(object : Callback<List<List<String>>> {
            override fun onResponse(call: Call<List<List<String>>>, response: Response<List<List<String>>>) {
                if (response.isSuccessful) {
                    Log.e(ContentValues.TAG, "네트워크 오류: dd")
                    val result = response.body()
                    result?.let{
                        handleTab1MyCustoms(it)
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
    private fun handleTab1MyCustoms(data: List<List<String>>) {
        for (record in data) {
            val getname = record[2]
            val getmenu = record[3]
            val getcustom = record[4]
            val getlikes = record[7]
            val getcategory = record[1]
            val getdescription = record[5]
            val getcreator = record[6]
            Log.d("plz name", "$getname")

            // favoriteItem 형식으로 변환
            val favoriteItem = FavoriteItem(getname, getcustom, getcategory)
            val myCustomItem = MyCustomsItem(getname, getmenu, getcustom, getlikes, getcategory, getdescription, getcreator)
            Log.d(ContentValues.TAG, "Added items to myCustomItemList: $myCustomItem")
            myCustomItemList.add(myCustomItem)
            Log.d(ContentValues.TAG, "Added items to collectItemList: $myCustomItemList")
            myCustomAdapter.notifyDataSetChanged()
        }
    }


    
    // add Customs
    private fun addNewCustom() {
        
    }





    companion object {
        private const val PHONE_ADD_REQUEST_CODE = 1
    }

    // 새로고침 로직
    override fun onRefresh() {
        favoriteItemList.clear()
        myCustomItemList.clear()
        collectItemListCoffee.clear()
        collectItemListNonCoffee.clear()
        collectItemListFrappuccino.clear()
        initiation() // 모든 view들 정의
        turnToggle() // Favorite Customs 구현 부분
        getAllMyCustom() // My Customs 구현 부분
        lifecycleScope.launch {
            delay(1000)
            clickCategory() // 각 카테고리 눌렀을 때 구현 부분
        }
        swipeRefreshLayout.isRefreshing = false
    }

}