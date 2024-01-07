package com.heewoong.brewstar

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyCustomAdapter(private var myCustomItemList: ArrayList<MyCustomsItem>) :
        RecyclerView.Adapter<MyCustomAdapter.MyCustomViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MyCustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_mycustom_recyclerview, parent, false)
        return MyCustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyCustomViewHolder, position: Int) {
        holder.bind(myCustomItemList[position])

        // 수정 버튼을 누르면 수정 창으로 넘어가기
        // 이거 안 된다. 다시 해봐야지...
        holder.btn_edit.setOnClickListener {
            fun onClick(view: View) {
                val intent = Intent(view.context, MyCustomEdit::class.java)
                view.context.startActivity(intent)
            }
        }

        // 전체 네모 버튼을 눌렀을 때도 상세정보 보기 해야하나?
    }

    override fun getItemCount(): Int {
        return myCustomItemList.size
    }

    // recyclerView 중 하나의 항목 삭제하는 함수
    private fun removeMyCustomItem(position: Int) {
        myCustomItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class MyCustomViewHolder(myCustomItemView: View) : RecyclerView.ViewHolder(myCustomItemView) {
        // Adapter에서 text나 button을 쓰려면, 여기서 선언해줘야 함
        val tv_name = myCustomItemView.findViewById<TextView>(R.id.tv_mycustom_name)
        val tv_menu = myCustomItemView.findViewById<TextView>(R.id.tv_mycustom_menu)
        val tv_custom = myCustomItemView.findViewById<TextView>(R.id.tv_mycustom_custom)
        val tv_likes = myCustomItemView.findViewById<TextView>(R.id.tv_mycustom_like)
        val btn_edit = myCustomItemView.findViewById<Button>(R.id.btn_mycustom_edit)

        // item의 name, menu, custom, likes를 할당해주는 함수
        fun bind(item: MyCustomsItem) {
            tv_name.text = item.name
            tv_menu.text = item.menu
            tv_custom.text = item.custom
            tv_likes.text = item.likes
        }
    }
}