package com.example.wym_002

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wym_002.databinding.DataItemBinding

class ItemAdapter(val listener: Listener): RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    private val itemList = ArrayList<ItemDataClass>()

    class ItemHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = DataItemBinding.bind(view)

        fun bind(itemDataClass: ItemDataClass, listener: Listener) = with(binding){

            imageViewCat.setImageResource(itemDataClass.iconCat)

            val string = itemDataClass.string
            textViewInfo.text = when (string.length > 14){
                true -> {
                    string.substring(0..11) + "..."
                }
                else -> {
                    string
                }
            }

            textViewDate.text = itemDataClass.date.substring(0..9)

            textViewSpend.text = itemDataClass.spend.toString()

            itemView.setOnClickListener(){
                listener.onClick(itemDataClass)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(itemList[position], listener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItemInList(item: ItemDataClass){
        itemList.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItemList(){
        itemList.clear()
        notifyDataSetChanged()
    }

    interface Listener {

        fun onClick(item: ItemDataClass)

    }

}