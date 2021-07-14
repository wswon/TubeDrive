package com.tube.driver.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tube.driver.databinding.ItemLoadMoreBinding
import com.tube.driver.databinding.ItemPlaceBinding

sealed class PlaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class Item(
        parent: ViewGroup,
        private val clickPlaceItem: (PlaceItem.Item) -> Unit,
        private val binding: ItemPlaceBinding =
            ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ) : PlaceViewHolder(binding.root) {
        fun bind(placeItem: PlaceItem.Item) {
            with(binding) {
                root.setOnClickListener {
                    clickPlaceItem(placeItem)
                }

                name.text = placeItem.name
                subCategory.text = placeItem.subCategory
                distance.text = placeItem.distance
                address.text = placeItem.addressName
                phoneNumber.text = placeItem.phoneNumber
            }
        }
    }

    class Footer(
        parent: ViewGroup,
        private val clickLoadMore: () -> Unit,
        binding: ItemLoadMoreBinding =
            ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ) : PlaceViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                clickLoadMore()
            }
        }
    }
}