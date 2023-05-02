package com.bella.sub1mystoryapps.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bella.sub1mystoryapps.DataPreferences
import com.bella.sub1mystoryapps.R
import com.bella.sub1mystoryapps.apirespon.ListStoryItem
import com.bella.sub1mystoryapps.databinding.ItemStoryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ListStoryAdapter(private val liststory: ArrayList<DataPreferences>) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private val list = ArrayList<DataPreferences>()
    private var ItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCalback (ItemClickCallback: OnItemClickCallback){
        this.ItemClickCallback = ItemClickCallback
    }

    fun setList(users: ArrayList<DataPreferences>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener { ItemClickCallback?.onItemClick(liststory[holder.adapterPosition]) }
    }

    inner class ListViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataPreferences){
            binding.root.setOnClickListener {
                ItemClickCallback?.onItemClick(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.name)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivStories)
                tvUsername.text = user.id
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClick(data: DataPreferences)
    }
}