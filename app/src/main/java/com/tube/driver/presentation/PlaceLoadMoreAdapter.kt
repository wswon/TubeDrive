package com.tube.driver.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class PlaceLoadMoreAdapter(
    private val clickLoadMore: () -> Unit,
) : ListAdapter<PlaceItem.LoadMoreFooter, PlaceViewHolder.LoadMore>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder.LoadMore {
        return PlaceViewHolder.LoadMore(parent, clickLoadMore)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder.LoadMore, position: Int) {

    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PlaceItem.LoadMoreFooter>() {
            override fun areItemsTheSame(oldItem: PlaceItem.LoadMoreFooter, newItem: PlaceItem.LoadMoreFooter): Boolean =
                true

            override fun areContentsTheSame(oldItem: PlaceItem.LoadMoreFooter, newItem: PlaceItem.LoadMoreFooter): Boolean =
                true
        }
    }
}
