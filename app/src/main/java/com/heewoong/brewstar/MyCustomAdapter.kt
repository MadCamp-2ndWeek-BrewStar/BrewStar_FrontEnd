package com.heewoong.brewstar

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityMyCustomEditBinding

class MyCustomAdapter(private var myCustomItemList: ArrayList<MyCustomsItem>) :
        RecyclerView.Adapter<MyCustomAdapter.MyCustomViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MyCustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_mycustom_recyclerview, parent, false)
        return MyCustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyCustomViewHolder, position: Int) {
        holder.bind(myCustomItemList[position])

        // 수정 버튼을 누르면 수정 창으로 넘어가기
        // 여기서 데이터들 다 받아와서 list에 반영까지 다 하기
        holder.btn_edit.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context, R.style.AlertDialogTheme)
            var bindingPopup = ActivityMyCustomEditBinding.inflate(LayoutInflater.from(holder.itemView.context))
            val view: View = bindingPopup.layoutPopup

            val oldName: String = holder.tv_name.text.toString()
            val oldMenu: String = holder.tv_menu.text.toString()
            val oldCustom: String = holder.tv_custom.text.toString()
            val oldLikes: String = holder.tv_likes.text.toString()

            builder.setView(view)
            view.findViewById<EditText>(R.id.editPopupName).setText(oldName)
            view.findViewById<EditText>(R.id.editPopupMenu).setText(oldMenu)
            view.findViewById<EditText>(R.id.editPopupCustom).setText(oldCustom)
            view.findViewById<TextView>(R.id.popuplikes).setText(oldLikes)
//            val intent = Intent(holder.itemView.context, MyCustomEdit::class.java)
//            intent.putExtra("old name", oldName)
//            holder.itemView.context.startActivity(intent)
            val alertDialog: AlertDialog = builder.create()

            // close 버튼 누르면 다시 돌아가기
            view.findViewById<ImageButton>(R.id.popupCloseBtn).setOnClickListener {
                alertDialog.dismiss()
            }

            var newName: String = ""
            var newMenu: String = ""
            var newCustom: String = ""

            // save 버튼 누르면 추가되기
            view.findViewById<Button>(R.id.popupSaveBtn).setOnClickListener {
                newName += view.findViewById<EditText>(R.id.editPopupName).text.toString()
                newMenu += view.findViewById<EditText>(R.id.editPopupMenu).text.toString()
                newCustom += view.findViewById<EditText>(R.id.editPopupCustom).text.toString()

                alertDialog.dismiss()
                
                // 좋아요 수는 그대로 유지해야 함
                myCustomItemList.add(MyCustomsItem(newName, newMenu, newCustom, oldLikes))
                removeMyCustomItem(position)
            }


            // 다이얼로그 형태 지우기
            if(alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            alertDialog.show()
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