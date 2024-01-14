package com.example.present.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.present.data.database.entities.FormItemEntity
import com.example.present.databinding.ItemFormBinding

class FormAdapter(private val presents: List<FormItemEntity>, private val context: Context) :
    RecyclerView.Adapter<FormAdapter.FormViewHolder>() {
    lateinit var binding: ItemFormBinding

    class FormViewHolder(private val binding: ItemFormBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: FormItemEntity, position: Int) {
                val number = position + 1
                binding.stage.text = "$number"
                try {
                    Glide.with(context).load(item.presentImg).into(binding.image)
                } catch (_: Exception) {}
                binding.text.text = item.congratulation
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        binding = ItemFormBinding.inflate(LayoutInflater.from(parent.context))
        return FormViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return presents.size
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val item: FormItemEntity = presents[position]
        holder.bind(item, position)
    }
}