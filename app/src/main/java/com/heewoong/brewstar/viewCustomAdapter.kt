package com.heewoong.brewstar

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityCustomDescriptionBinding
import java.util.*

class viewCustomAdapter(private val context: Context, private var datas: List<CustomItem>) : RecyclerView.Adapter<viewCustomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val pic: ImageView = itemView.findViewById(R.id.iv_mycustom_menu)
        private val name: TextView = itemView.findViewById(R.id.customName)
        private val menu: TextView = itemView.findViewById(R.id.menu)
        private val option: TextView = itemView.findViewById(R.id.customOption)
        private val creator: TextView = itemView.findViewById(R.id.customCreator)
        private val likes: TextView = itemView.findViewById(R.id.likes)
        private val showDescription: ImageView = itemView.findViewById(R.id.custom_rectangle)

        var id: String =""
        var description: String = ""
        var category: String = ""

        private lateinit var bindingPopup: ActivityCustomDescriptionBinding

//        init {
//            showDescription.setOnClickListener {
//                showCustomDescription()
//            }
//        }

        fun bindView(item: CustomItem, context: Context) {

            id = item.id
            description= item.description
            category =item.category

            if (item.category == "Coffee") {
                pic.setImageResource(R.drawable.coffee)
            } else if (item.category == "Non-Coffee") {
                pic.setImageResource(R.drawable.noncoffee)
            } else {
                pic.setImageResource(R.drawable.frappuccino)
            }

            name.setText(item.name)
            menu.setText(item.menu)
            option.setText(item.custom)
            creator.setText(item.creator)
            likes.setText(item.likes.toString())

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
    }

    fun setItems(list: ArrayList<CustomItem>) {
        datas = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_custom_menu_item, parent, false))

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(datas[position], context)
    }
}