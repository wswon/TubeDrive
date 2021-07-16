package com.tube.driver.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class PlaceAdapter(
    private val clickPlaceItem: (PlaceItem.Item) -> Unit
) : ListAdapter<PlaceItem.Item, PlaceViewHolder.Item>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder.Item {
        return PlaceViewHolder.Item(parent, clickPlaceItem)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder.Item, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PlaceItem.Item>() {
            override fun areItemsTheSame(oldItem: PlaceItem.Item, newItem: PlaceItem.Item): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PlaceItem.Item, newItem: PlaceItem.Item): Boolean =
                oldItem == newItem
        }
    }
}