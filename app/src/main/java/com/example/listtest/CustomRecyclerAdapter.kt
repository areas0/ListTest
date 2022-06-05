package com.example.listtest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomRecyclerAdapter(
    val context: Context,
    val data: MutableList<GameObject>,
    private val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {
    // the new RecyclerAdapter enforces the use of the ViewHolder performance pattern
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.game_name)
        val imageView : ImageView = itemView.findViewById(R.id.icon_display)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // called when a new viewholder is required to display a row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create the row from a layout inflater
        val rowView = LayoutInflater
            .from(context)
            .inflate(R.layout.list_item_game, parent, false)
        // attach the onclicklistener
        rowView.setOnClickListener(onItemClickListener)
        // create a ViewHolder using this rowview
        val viewHolder = ViewHolder(rowView)
        // return this ViewHolder. The system will make sure view holders
        // are used and recycled
        return viewHolder
    }

    // called when a row is about to be displayed
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // retrieve the item at the specified position
        val currentItem = data[position]
        // put the data
        holder.nameTextView.text = currentItem.name
        Glide
            .with(context)
            .load(currentItem.picture)
            .into(holder.imageView)
        // store row position inside view tag
        holder.itemView.tag = currentItem.id
    }
}