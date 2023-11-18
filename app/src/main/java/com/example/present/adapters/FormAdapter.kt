package com.example.present.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.present.data.database.entities.FormItemEntity
import com.example.present.databinding.ItemFormBinding
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class FormAdapter(private val presents: List<FormItemEntity>, private val context: Context) :
    RecyclerView.Adapter<FormAdapter.FormViewHolder>() {
    lateinit var binding: ItemFormBinding

    class FormViewHolder(private val binding: ItemFormBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: FormItemEntity) {
                binding.stage.text = item.idStage.toString()
                //TODO: Разобраться с отображением фото
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
        holder.bind(item)
    }
}