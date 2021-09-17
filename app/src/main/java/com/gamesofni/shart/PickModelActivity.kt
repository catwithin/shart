package com.gamesofni.shart

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamesofni.shart.data.Datasource
import com.gamesofni.shart.data.ItemAdapter
import com.gamesofni.shart.data.Model3d
import com.gamesofni.shart.data.MyItemDetailsLookup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_pick_model.*

class PickModelActivity : AppCompatActivity() {
    var tracker: SelectionTracker<Long>? = null
    var name: String? = null
    private lateinit var doneButton: FloatingActionButton
    private lateinit var models: List<Model3d>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_model)

        doneButton = findViewById(R.id.fab_done)
        doneButton.setOnClickListener{
//            Toast.makeText(this, "FAB is clicked...", Toast.LENGTH_LONG).show()
            val intent = Intent()
            intent.putExtra("modelName", name)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        doneButton.isEnabled = tracker?.selection?.size() == 1

        // Initialize data.
        models = Datasource().loadModels()

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
                        name = models[tracker?.selection!!.first().toInt()].name
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
