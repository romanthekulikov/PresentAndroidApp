package com.example.present.adapters

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.present.data.database.entities.FormItemEntity
import com.example.present.databinding.ItemFormBinding

class FormAdapter(private val presents: List<FormItemEntity>) :
    RecyclerView.Adapter<FormAdapter.FormViewHolder>() {
    lateinit var binding: ItemFormBinding

    class FormViewHolder(private val binding: ItemFormBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: FormItemEntity) {
                binding.stage.text = item.idStage.toString()
                try {
                    binding.image.setImageURI(Uri.parse(item.presentImg))
                } catch (_: Exception) {}
                //MediaStore.Images.Media.getContentUri(item.presentImg)

                binding.text.text = item.congratulation
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        binding = ItemFormBinding.inflate(LayoutInflater.from(parent.context))
        return FormViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return presents.size
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val item: FormItemEntity = presents[position]
        item.idStage = position + 1
        holder.bind(item)
    }
}