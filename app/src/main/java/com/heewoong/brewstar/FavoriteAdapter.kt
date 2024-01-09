package com.heewoong.brewstar

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityCustomDescriptionBinding

class FavoriteAdapter (private var FavoriteItemList: ArrayList<FavoriteItem>) :
        RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_favorite_recyclerview, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(FavoriteItemList[position])

//         별 버튼 누르면 리스트에서 빠지기
        holder.btn_like.setOnClickListener {
//            holder.btn_like.setImageResource(R.drawable.unlike)
            // wish = X로 바꾸기
            removeMyCustomItem(position)
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

        // item의 name, menu, custom, likes를 할당해주는 함수
        fun bind(item: FavoriteItem) {
            tv_name.text = item.name
            tv_custom.text = item.custom
        }

        init {
            viewCard.setOnClickListener {
                showCustomDescription()
            }
        }

        private fun showCustomDescription() {
            val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context, R.style.AlertDialogTheme)
            bindingPopup = ActivityCustomDescriptionBinding.inflate(LayoutInflater.from(context))
            val view: View = bindingPopup.layoutPopupDescription

            builder.setView(view)

            val alertDialog: AlertDialog = builder.create()

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

    }
}