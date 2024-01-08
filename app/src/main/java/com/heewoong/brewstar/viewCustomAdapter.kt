package com.heewoong.brewstar

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.heewoong.brewstar.databinding.ActivityCustomDescriptionBinding

class viewCustomAdapter(private val context: Context, private val datas: List<customDummy>) : RecyclerView.Adapter<viewCustomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = itemView.findViewById(R.id.customName)
        private val menu: TextView = itemView.findViewById(R.id.menu)
        private val option: TextView = itemView.findViewById(R.id.customOption)
        private val creator: TextView = itemView.findViewById(R.id.customCreator)
        private val likes: TextView = itemView.findViewById(R.id.likes)
        private val showDescription: ImageView = itemView.findViewById(R.id.custom_rectangle)
        private lateinit var bindingPopup: ActivityCustomDescriptionBinding

        init {
            showDescription.setOnClickListener {
                showCustomDescription()
            }
        }

        fun bindView(item: customDummy, context: Context) {
            name.text = item.name
            menu.text = item.menu
            option.text = item.option
            creator.text = item.creator
            likes.text = item.likes.toString()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_custom_menu_item, parent, false))

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(datas[position], context)
    }
}