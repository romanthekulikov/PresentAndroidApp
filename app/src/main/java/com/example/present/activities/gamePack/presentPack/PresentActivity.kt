package com.example.present.activities.gamePack.presentPack

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.present.databinding.ActivityPresentBinding
import com.example.present.domain.IntentKeys
import com.example.present.activities.gamePack.getPresentPack.GetPresentActivity
import com.example.present.adapters.PresentListAdapter
import com.example.present.data.database.AppDatabase
import com.example.present.data.models.PresentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PresentActivity : AppCompatActivity(), PresentListAdapter.PresentAdapterFunc {
    private lateinit var binding: ActivityPresentBinding
    private lateinit var presentVM: PresentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presentVM =
            ViewModelProvider(this, PresentViewModelFactory(this))[PresentViewModel::class.java]
        presentVM.getData()
        presentListInit()
        listenersInit()
    }

    private fun presentListInit() {
        CoroutineScope(Dispatchers.IO).launch {
            val stageDao = AppDatabase.getDB(this@PresentActivity).getStageDao()
            val stages = stageDao.getAllStage()
            val listPresent = mutableListOf<PresentModel>()
            for (stage in stages) {
                listPresent.add(PresentModel(stage.idPresent, stage.isDone))
            }
            CoroutineScope(Dispatchers.Main).launch {
                val presentAdapter = PresentListAdapter(listPresent, this@PresentActivity)
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@PresentActivity)
                binding.presentList.layoutManager = layoutManager
                binding.presentList.adapter = presentAdapter
            }
        }
    }

    private fun listenersInit() {
        binding.apply {
            back.setOnClickListener {
                finish()
            }
        }
    }

    override fun onButtonClick(presentId: Int) {
        val intent = Intent(this, GetPresentActivity::class.java)
        intent.putExtra(IntentKeys.PROGRESS_KEY, presentId)

        startActivity(intent)
    }
}