package com.example.calorietracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calorietracker.databinding.ItemMenuBinding

typealias OnClickAdd = (Menu) -> Unit

class MenuAdapter(private val listMenus: List<Menu>,
                  private val onClickAdd: OnClickAdd) :
    RecyclerView.Adapter<MenuAdapter.ItemMenuViewHolder>() {

    inner class ItemMenuViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Menu) {
            with(binding) {
                txtNamaMakanan.text = data.nama
                txtJumlahKalori.text = data.jumlah

                itemView.setOnClickListener {
                    onClickAdd(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuAdapter.ItemMenuViewHolder {
        val binding = ItemMenuBinding.inflate(
            LayoutInflater.from(parent.context),parent,
            false)
        return ItemMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMenuViewHolder, position: Int) {
        holder.bind(listMenus[position])
    }

    override fun getItemCount(): Int {
        return listMenus.size
    }
}