package com.heewoong.brewstar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteAdapter (private var FavoriteItemList: ArrayList<FavoriteItem>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_favorite_recyclerview, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(FavoriteItemList[position])

        // 별 버튼 누르면 리스트에서 빠지기
        // 이거 안 된다. 다시 해봐야지...
//        holder.btn_like.setOnClickListener {
//            holder.btn_like.setImageResource(R.drawable.unlike)
//            }
//        }

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
        //val btn_like = favoriteItemView.findViewById<Button>(R.id.btn_like)

        // item의 name, menu, custom, likes를 할당해주는 함수
        fun bind(item: FavoriteItem) {
            tv_name.text = item.name
            tv_custom.text = item.custom
        }
    }
}