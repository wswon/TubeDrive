package com.tube.driver.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class PlaceAdapter(
    private val clickPlaceItem: (PlaceItem.Item) -> Unit,
    private val clickLoadMore: () -> Unit,
) : ListAdapter<PlaceItem, PlaceViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return when (viewType) {
            TYPE_ITEM -> PlaceViewHolder.Item(parent, clickPlaceItem)
            TYPE_FOOTER -> PlaceViewHolder.Footer(parent, clickLoadMore)
            else -> error("Invalid viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        if (holder is PlaceViewHolder.Item) {
            holder.bind(currentList[position] as PlaceItem.Item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is PlaceItem.Item -> TYPE_ITEM
            is PlaceItem.LoadMoreFooter -> TYPE_FOOTER
        }
    }

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2

        val diffCallback = object : DiffUtil.ItemCallback<PlaceItem>() {
            override fun areItemsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean =
                oldItem == newItem
        }
    }
}