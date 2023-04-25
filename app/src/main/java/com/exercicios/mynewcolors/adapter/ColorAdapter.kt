package com.exercicios.mynewcolors.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exercicios.mynewcolors.OnItemClickRecyclerView
import com.exercicios.mynewcolors.R
import com.exercicios.mynewcolors.model.MyColor
import com.exercicios.mynewcolors.model.MyColorList
import java.util.*

class ColorAdapter(private val myColorList: MyColorList): RecyclerView.Adapter<ColorAdapter.MyHolder>() {

    var onItemClickRecyclerView: OnItemClickRecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.color_item, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val bindedColor = this.myColorList.getColor(position)
        holder.tvColorName.text = bindedColor.name
        holder.tvColorHex.text = bindedColor.getHexCode()
        holder.ivColorIcon.setColorFilter(bindedColor.getColor())
    }

    override fun getItemCount(): Int {
        return this.myColorList.getSize()
    }

    fun add(color: MyColor) {
        this.myColorList.save(color)
        this.notifyDataSetChanged()
    }

    fun del(index: Int) {
        this.myColorList.removeAt(index)
        this.notifyItemRemoved(index)
        this.notifyItemRangeChanged(index, this.myColorList.getSize())
    }

    fun move(from: Int, to: Int) {
        Collections.swap(this.myColorList.getList(), from, to)
        this.notifyItemMoved(from, to)
    }

    inner class MyHolder(item: View): RecyclerView.ViewHolder(item) {
        var tvColorName: TextView
        var tvColorHex: TextView
        var ivColorIcon: ImageView

        init {
            this.tvColorName = item.findViewById(R.id.tvColorName)
            this.tvColorHex = item.findViewById(R.id.tvColorHex)
            this.ivColorIcon = item.findViewById(R.id.ivColorIcon)

            item.setOnClickListener {
                this@ColorAdapter.onItemClickRecyclerView?.onItemClick(this.adapterPosition)
            }
        }
    }
}