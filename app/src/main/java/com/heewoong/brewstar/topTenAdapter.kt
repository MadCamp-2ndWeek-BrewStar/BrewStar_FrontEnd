package com.heewoong.brewstar

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityCustomDescriptionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class topTenAdapter(private val context: Context,
                    private val datas: List<CustomItem>) : RecyclerView.Adapter<topTenAdapter.ViewHolder>(){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val pic: ImageView = itemView.findViewById(R.id.customPic)
        private val name: TextView = itemView.findViewById(R.id.customName)
        private val option: TextView = itemView.findViewById(R.id.customOption)
        private val creator: TextView = itemView.findViewById(R.id.customCreator)
        private val likes: TextView = itemView.findViewById(R.id.likes)
        private val showDescription: ImageView = itemView.findViewById(R.id.top10_rectangle)

        var id :String = ""
        var menu : String = ""
        var description : String = ""
        var category : String = ""

        private lateinit var bindingPopup: ActivityCustomDescriptionBinding

        val api = RetrofitInterface.create()
        // 토큰 아이디
        private lateinit var tokenId: String
        // 좋아요 눌렀는지 판단하기위해 만든 favoriteItemList
        private var favoriteItemList = ArrayList<MyCustomsItem>()
        private var checkLike = false

        fun bindView(item: CustomItem, context: Context) {

            val sharedPref = context.getSharedPreferences("getTokenId", Context.MODE_PRIVATE)
            tokenId = sharedPref.getString("tokenId", "")!!

            id = item.id
            menu= item.menu
            description = item.description
            category = item.category

            if (item.category == "Coffee") {
                pic.setImageResource(R.drawable.coffee)
            } else if (item.category == "Non-Coffee") {
                pic.setImageResource(R.drawable.noncoffee)
            } else {
                pic.setImageResource(R.drawable.frappuccino)
            }

            name.setText(item.name)
            option.setText(item.custom)
            creator.setText(item.creator)
            likes.setText(item.likes.toString())

            getTopTenFavorite() // favorite 누른 애들의 리스트
            // 그러면, 여기에 들어온 CustomItem이 favoriteItemList 안에 있는지 판단해야 한다.
            Handler(Looper.getMainLooper()).postDelayed({
                checkLike = checkItemLike(item) // item이 like 누른 애인지 판단
            }, 1000)

            fun showCustomDescription() {
                val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context, R.style.AlertDialogTheme)
                bindingPopup = ActivityCustomDescriptionBinding.inflate(LayoutInflater.from(context))
                val view: View = bindingPopup.layoutPopupDescription

                val getName: String = item.name
                val getMenu: String = item.menu
                val getCustom: String = item.custom
                val getLikes: String = item.likes
                val getDescription: String = item.description
                val getCreator: String = item.creator
                val getCategory: String = item.category

                builder.setView(view)

                view.findViewById<TextView>(R.id.popuplikes).setText(getLikes)
                view.findViewById<TextView>(R.id.editPopupName).setText(getName)
                view.findViewById<TextView>(R.id.editPopupMenu).setText(getMenu)
                view.findViewById<TextView>(R.id.editPopupCustom).setText(getCustom)
                view.findViewById<TextView>(R.id.editPopupDescription).setText(getDescription)
                view.findViewById<TextView>(R.id.popupMadeBy).setText(getCreator)
                if (checkLike) {
                    view.findViewById<ImageView>(R.id.popupStar).setImageResource(R.drawable.like2)
                }

                if (getCategory == "Coffee") {
                    view.findViewById<ImageView>(R.id.popupImage).setImageResource(R.drawable.coffee)
                } else if (getCategory == "Non-Coffee") {
                    view.findViewById<ImageView>(R.id.popupImage).setImageResource(R.drawable.noncoffee)
                } else {
                    view.findViewById<ImageView>(R.id.popupImage).setImageResource(R.drawable.frappuccino)
                }

                val alertDialog: AlertDialog = builder.create()

                // close 버튼 누르면 다시 돌아가기
                view.findViewById<ImageButton>(R.id.popupCloseBtn).setOnClickListener {
                    alertDialog.dismiss()
                }

                view.findViewById<ImageView>(R.id.popupStar).setOnClickListener {
                    val call = api.likeCustom(tokenId, id)
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
                    if (checkLike) {
                        // 좋아요 누른 걸 취소한거면
                        view.findViewById<ImageView>(R.id.popupStar).setImageResource(R.drawable.unlike)
                        checkLike = false
                    } else {
                        view.findViewById<ImageView>(R.id.popupStar).setImageResource(R.drawable.like2)
                        checkLike = true
                    }
                }


                // 다이얼로그 형태 지우기
                if(alertDialog.window != null) {
                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                }
                alertDialog.show()
            }

            showDescription.setOnClickListener{
                showCustomDescription()
            }



        }

        // favorite 누른 애들 구하기
        private fun getTopTenFavorite() {
            // data를 받아서, favoriteItemList에 add하면 됨.
            api.getFavorite(tokenId!!).enqueue(object : Callback<List<List<String>>> {
                override fun onResponse(call: Call<List<List<String>>>, response: Response<List<List<String>>>) {
                    if (response.isSuccessful) {
                        Log.e(ContentValues.TAG, "네트워크 오류: dd")
                        val result = response.body()
                        result?.let{
                            handleTopTenFavorite(it)
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
        private fun handleTopTenFavorite(data: List<List<String>>) {
            for (record in data) {
                val getCustomId = record[0]
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
                val favoriteItem = MyCustomsItem(getCustomId, getname, getmenu, getcustom, getlikes, getcategory, getdescription, getcreator)
                Log.d(ContentValues.TAG, "Added items to collectItemList: $favoriteItem")
                favoriteItemList.add(favoriteItem)
                Log.d(ContentValues.TAG, "Added items to collectItemList: $favoriteItemList")
//            favoriteAdapter.notifyDataSetChanged()
            }
        }


        private fun checkItemLike(item: CustomItem): Boolean {
            var checklike = false
            for (favoriteitem in favoriteItemList) {
                if (item.id == favoriteitem.customid) {
                    checklike = true
                    break
                }
            }
            return checklike
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_custom,parent,false))


    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(datas[position], context)
    }

}