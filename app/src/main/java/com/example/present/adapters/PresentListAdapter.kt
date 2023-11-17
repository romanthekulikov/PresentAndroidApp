package com.example.present.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.present.data.models.PresentModel
import com.example.present.databinding.ItemPresentBinding

class PresentListAdapter(
    private var presentList: List<PresentModel>,
    private val progress: Int = 0,
    private val listener: PresentAdapterFunc
) : RecyclerView.Adapter<PresentListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPresentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPresentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(presentList[position]) {
                binding.present.text = this.name
                if (this.accessLevel <= progress) {
                    binding.present.alpha = 1f
                    binding.present.setOnClickListener {
                        listener.onButtonClick(this.accessLevel - 1)
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return presentList.size
    }

    interface PresentAdapterFunc {
        fun onButtonClick(presentId: Int)
    }
}