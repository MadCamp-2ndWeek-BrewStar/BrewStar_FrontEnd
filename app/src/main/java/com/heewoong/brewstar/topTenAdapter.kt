package com.heewoong.brewstar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class topTenAdapter(private val context: Context,
                    private val datas: List<topTenDummy>) : RecyclerView.Adapter<topTenAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = itemView.findViewById(R.id.customName)
        private val option: TextView = itemView.findViewById(R.id.customOption)
        private val creator: TextView = itemView.findViewById(R.id.customCreator)
        private val likes: TextView = itemView.findViewById(R.id.likes)

        fun bindView(item: topTenDummy, context: Context) {
            name.setText(item.name)
            option.setText(item.option)
            creator.setText(item.creator)
            likes.setText(item.likes.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_custom,parent,false))


    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(datas[position], context)
    }



}