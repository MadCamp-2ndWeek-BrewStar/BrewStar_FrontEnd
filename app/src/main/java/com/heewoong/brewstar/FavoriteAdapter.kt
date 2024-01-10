package com.heewoong.brewstar

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
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

class FavoriteAdapter (private var FavoriteItemList: ArrayList<MyCustomsItem>) :
        RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    // 서버에서 불러오기
    val api = RetrofitInterface.create()
    // 토큰 아이디
    private lateinit var tokenId: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_favorite_recyclerview, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(FavoriteItemList[position])
        val sharedPref = holder.itemView.context.getSharedPreferences("getTokenId", Context.MODE_PRIVATE)
        tokenId = sharedPref.getString("tokenId", "")!!
        val customId = FavoriteItemList[position].customid

//         별 버튼 누르면 리스트에서 빠지기
        holder.btn_like.setOnClickListener {
//            holder.btn_like.setImageResource(R.drawable.unlike)
            // wish = X로 바꾸기
            Log.d("여기에 안 들어가나", "여기에 안 들어가나")
            Log.d("여기에 안 들어가나", tokenId)
            Log.d("여기에 안 들어가나", customId)
            val call = api.likeCustom(tokenId, customId)
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
            holder.btn_like.setImageResource(R.drawable.unlike)

//            removeMyCustomItem(position)
        }
        
        // 뷰카드 누르면 상세정보 뜨기
        holder.viewCard.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context, R.style.AlertDialogTheme)
            var bindingPopup = ActivityCustomDescriptionBinding.inflate(LayoutInflater.from(holder.itemView.context))
            val view: View = bindingPopup.layoutPopupDescription


            // activity의 각 요소들을 원래 data들로 채우는 과정
            val favoriteItemOne = FavoriteItemList[position]
            val oldCustomId: String = favoriteItemOne.customid
            val oldName: String = favoriteItemOne.name
            val oldMenu: String = favoriteItemOne.menu
            val oldCustom: String = favoriteItemOne.custom
            val oldLikes: String = favoriteItemOne.likes
            val oldCategory: String =favoriteItemOne.category
            val oldDescription: String = favoriteItemOne.description
            val oldCreator: String = favoriteItemOne.creator

            builder.setView(view)
            view.findViewById<TextView>(R.id.editPopupName).setText(oldName)
            view.findViewById<TextView>(R.id.editPopupMenu).setText(oldMenu)
            view.findViewById<TextView>(R.id.editPopupCustom).setText(oldCustom)
            view.findViewById<TextView>(R.id.popuplikes).setText(oldLikes)
            view.findViewById<TextView>(R.id.editPopupDescription).setText(oldDescription)
            view.findViewById<TextView>(R.id.popupMadeBy).setText(oldCreator)
            view.findViewById<ImageView>(R.id.popupStar).setImageResource(R.drawable.like2)
            if (oldCategory == "Coffee") {
                view.findViewById<ImageView>(R.id.popupImage).setImageResource(R.drawable.coffee)
            } else if (oldCategory == "Non-Coffee") {
                view.findViewById<ImageView>(R.id.popupImage).setImageResource(R.drawable.noncoffee)
            } else {
                view.findViewById<ImageView>(R.id.popupImage).setImageResource(R.drawable.frappuccino)
            }

            // builder create
            val alertDialog: AlertDialog = builder.create()

            view.findViewById<ImageView>(R.id.popupStar).setOnClickListener {
                // 큰 화면에서 좋아요 누른 걸 취소한거면,
                val call = api.likeCustom(tokenId, customId)
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
                view.findViewById<ImageView>(R.id.popupStar).setImageResource(R.drawable.unlike)
            }

            // close 버튼 누르면 다시 돌아가기
            view.findViewById<ImageButton>(R.id.popupCloseBtn).setOnClickListener {
                alertDialog.dismiss()
            }

            // 다이얼로그 형태 지우기
            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            alertDialog.show()
        }

        // 전체 네모 버튼을 눌렀을 때도 상세정보 보기 해야하나?
    }

    override fun getItemCount(): Int {
        return FavoriteItemList.size
    }

    // recyclerView 중 하나의 항목 삭제하는 함수
    private fun removeMyCustomItem(position: Int) {
        FavoriteItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class FavoriteViewHolder(favoriteItemView: View) : RecyclerView.ViewHolder(favoriteItemView) {
        // Adapter에서 text나 button을 쓰려면, 여기서 선언해줘야 함
        val tv_name = favoriteItemView.findViewById<TextView>(R.id.tv_favorite_name)
        val tv_custom = favoriteItemView.findViewById<TextView>(R.id.tv_favorite_custom)
        val btn_like = favoriteItemView.findViewById<ImageButton>(R.id.btn_like)
        val viewCard = favoriteItemView.findViewById<ImageView>(R.id.favorite_rectangle)
        val picture = favoriteItemView.findViewById<ImageView>(R.id.iv_favorite_menu)

        // item의 name, menu, custom, likes를 할당해주는 함수
        fun bind(item: MyCustomsItem) {
            tv_name.text = item.name
            tv_custom.text = item.custom
            if (item.category == "Coffee") {
                picture.setImageResource(R.drawable.coffee)
            } else if (item.category == "Non-Coffee") {
                picture.setImageResource(R.drawable.noncoffee)
            } else {
                picture.setImageResource(R.drawable.frappuccino)
            }
        }
    }
}