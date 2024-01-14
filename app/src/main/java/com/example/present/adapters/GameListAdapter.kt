package com.example.present.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.present.databinding.GameItemBinding
import com.example.present.remote.model.GamesProgressResponse

class GamesProgressAdapter(private val gameList: List<GamesProgressResponse>, private val idUser: Int) :
    RecyclerView.Adapter<GamesProgressAdapter.ProgressViewHolder>() {
    lateinit var binding: GameItemBinding
    class ProgressViewHolder(private val binding: GameItemBinding, private val idUser: Int) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GamesProgressResponse) {
            if (item.id_admin == idUser) {
                binding.adminIcon.alpha = 1f
            }
            binding.progressString.text = item.progress_string
            binding.progressBar.setProgress((item.progress).toInt(), true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        binding = GameItemBinding.inflate(LayoutInflater.from(parent.context))
        return ProgressViewHolder(binding, idUser)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        val item: GamesProgressResponse = gameList[position]
        holder.bind(item)
    }
}