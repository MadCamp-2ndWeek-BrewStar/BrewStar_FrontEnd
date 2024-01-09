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
import com.heewoong.brewstar.databinding.ActivityMyCustomAddBinding
import java.util.Collections

class MyCustomAdapter(private var myCustomItemList: ArrayList<MyCustomsItem>) :
        RecyclerView.Adapter<MyCustomAdapter.MyCustomViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MyCustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_mycustom_recyclerview, parent, false)
        val context = parent.context
        return MyCustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyCustomViewHolder, position: Int) {
        holder.bind(myCustomItemList[position])

        // 수정 버튼을 누르면 수정 창으로 넘어가기
        // 여기서 데이터들 다 받아와서 list에 반영까지 다 하기
        holder.btn_edit.setOnClickListener {

            // 하지만 나중에는, 수정 버튼을 눌렀을 때 데이터베이스에서 모든 정보가 불러와져야 함
            // 임시로 지금 나는 이미 적혀있는 내용들을 불러왔을 뿐, 원래는 데이터베이스에서 정보를 불러와야!
            // 이건 id를 이용해서 불러올 수 있을 것 같음.
            // 또는, name, menu, custom을 통해서 불러올 수 있지 않을까 싶다. 똑같은 건 추가 안된다는 가정하에.
            // 그걸 불러와서, 수정 창에는 name/menu/custom/description/creator/likes/wish까지 모두 불러와지겠지.
            // 내가 내 걸 좋아요 누를 수도 있는 거니까! 아니 근데 좋아요 못 누르는 걸로 하자 일단은 ㅎ
            // 어쨌든 My Customs에는, creator이 자기 자신인 경우만 리스트 해두면 됨.

            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context, R.style.AlertDialogTheme)
//            var bindingPopup = ActivityMyCustomEditBinding.inflate(LayoutInflater.from(holder.itemView.context))
            var bindingPopup = ActivityMyCustomAddBinding.inflate(LayoutInflater.from(holder.itemView.context))
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
                
                // 이것도 원래 데이터베이스에서 이러한 걸 수정하는 쿼리문을 만들어서 그걸 반영해야 할 듯.
                // 이건 해봤자 name, menu, custom, description만 수정하는 거니까!
                // 좋아요도 수정하는 건... 그건 save와는 별개로 쳐야하나. 일단은 빼고 생각하자.
                
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
    fun removeMyCustomItem(position: Int) {
        // 아하 삭제 기능 구현해야 하는데. 슬라이드 해서 삭제하는 방식으로 가야겠다.
        myCustomItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    // 데이터 전체 값 갱신
    private fun setMyCustomItem(myCustomItemList: ArrayList<MyCustomsItem>) {
        this.myCustomItemList = myCustomItemList
        notifyDataSetChanged()
    }

    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(myCustomItemList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
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