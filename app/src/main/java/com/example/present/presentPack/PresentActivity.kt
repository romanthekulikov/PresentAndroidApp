package com.example.present.presentPack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.present.data.PresentModel
import com.example.present.data.StringProvider
import com.example.present.databinding.ActivityPresentBinding
import com.example.present.domain.IntentKeys
import com.example.present.getPresentPack.GetPresentActivity

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
        val presentList = getPresentList()
        val presentAdapter = PresentListAdapter(presentList, presentVM.progress.value!!, this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.presentList.layoutManager = layoutManager
        binding.presentList.adapter = presentAdapter
    }

    private fun listenersInit() {
        binding.apply {
            back.setOnClickListener {
                finish()
            }
        }
    }

    private fun getPresentList(): List<PresentModel> {
        val presentList: MutableList<PresentModel> = mutableListOf()

        val presentNames = StringProvider.presentNameList
        val size = presentNames.size
        for (i in 0 until size) {
            presentList.add(PresentModel(presentNames[i], i + 1))
        }

        return presentList
    }

    override fun onButtonClick(presentId: Int) {
        val intent = Intent(this, GetPresentActivity::class.java)
        intent.putExtra(IntentKeys.PROGRESS_KEY, presentId)

        startActivity(intent)
    }
}