package com.tube.driver.presentation.place.adapter

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

    override fun onBindViewHolder(
        holder: PlaceViewHolder.Item,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (isSelectedChangePayload(payloads)) {
            holder.changeSelected(currentList[position])
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun isSelectedChangePayload(payloads: MutableList<Any>) =
        payloads.any { it == CHANGE_SELECTED }

    companion object {
        private const val CHANGE_SELECTED = "change_selected"

        val diffCallback = object : DiffUtil.ItemCallback<PlaceItem.Item>() {
            override fun areItemsTheSame(oldItem: PlaceItem.Item, newItem: PlaceItem.Item): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PlaceItem.Item, newItem: PlaceItem.Item): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: PlaceItem.Item, newItem: PlaceItem.Item): Any? {
                if (oldItem.isSelected != newItem.isSelected) {
                    return CHANGE_SELECTED
                }
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }
}