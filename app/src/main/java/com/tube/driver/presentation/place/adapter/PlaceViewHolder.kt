package com.tube.driver.presentation.place.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tube.driver.R
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

                changeSelected(placeItem)

                name.text = placeItem.name
                subCategory.text =
                    if (placeItem.subCategory.isNotEmpty()) placeItem.subCategory else placeItem.category
                address.text = placeItem.addressName
                phoneNumber.text = placeItem.phoneNumber
            }
        }

        fun changeSelected(placeItem: PlaceItem.Item) {
            binding.root.background = ContextCompat.getDrawable(
                itemView.context,
                if (placeItem.isSelected) R.color.fff5f7fb else R.color.white
            )
        }
    }
}