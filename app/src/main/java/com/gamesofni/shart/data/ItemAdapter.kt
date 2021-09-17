package com.gamesofni.shart.data

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.gamesofni.shart.R


class ItemAdapter(
    private val context: Context,
    private val dataset: List<Model3d>,
    var tracker: SelectionTracker<Long>? = null
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    init {
        setHasStableIds(true)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imageView: ImageView = view.findViewById(R.id.item_image)

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
                // to select on single short touch
                override fun inSelectionHotspot(e: MotionEvent): Boolean { return true }
            }

        fun bind(value: Int, isActivated: Boolean = false) {
//            text.text = value.toString()
            itemView.isActivated = isActivated
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.pick_model_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
//        holder.textView.text = context.resources.getString(item.name)
        holder.textView.text = item.name
        holder.imageView.setImageResource(item.previewResourceId)
        tracker?.let {
            holder.bind(position, it.isSelected(position.toLong()))
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size
//    Same as
//    override fun getItemCount(): Int {
//        return dataset.size
//    }

//    fun setTracker(tracker: SelectionTracker<Long>?) {
//        this.tracker = tracker
//    }

    override fun getItemId(position: Int): Long = position.toLong()
}

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as ItemAdapter.ItemViewHolder)
                .getItemDetails()
        }
        return null
    }
}
