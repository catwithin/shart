package com.gamesofni.shart

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamesofni.shart.data.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_pick_model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PickModelActivity : AppCompatActivity() {
    private var id: Int? = null
    var tracker: SelectionTracker<Long>? = null
//    var name: String? = null
    private lateinit var doneButton: FloatingActionButton
    private lateinit var models: List<Model3d>

    private val viewModel: ShartViewModel by viewModels<ShartViewModel>() {
        ShartViewModelFactory(
            (application as ShartApplication).database.model3dDao(),
            (application as ShartApplication).database.shartObjectDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_model)

        doneButton = findViewById(R.id.fab_done)
        doneButton.setOnClickListener{
//            Toast.makeText(this, "FAB is clicked...", Toast.LENGTH_LONG).show()
            val intent = Intent()
            intent.putExtra("modelId", id)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        doneButton.isEnabled = tracker?.selection?.size() == 1

        // Initialize data.
//        val applicationScope = CoroutineScope(SupervisorJob())
//        applicationScope.launch {
            models = viewModel.all3dModels
//        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // Make a grid layout manager
        GridLayoutManager(this,2, RecyclerView.VERTICAL,false )
            .apply { recyclerView.layoutManager = this }

        val adapter = ItemAdapter(this, models)
        recyclerView.adapter = adapter

        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
        .build()

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val items = tracker?.selection!!.size()
                    if (items == 1) {
                        id = models[tracker?.selection!!.first().toInt()].id
                    }
                    doneButton.isEnabled = items == 1
                }
        })

        adapter.tracker = tracker



        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }
}
