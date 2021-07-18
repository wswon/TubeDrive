package com.tube.driver.presentation.place.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import com.tube.driver.R
import com.tube.driver.databinding.ViewCategoryButtonBinding
import com.tube.driver.presentation.place.CategoryType
import com.tube.driver.util.dp

class CategoryButtonListLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private var changeCategory: ((CategoryType) -> Unit)? = null

    init {
        addCategoryButton(CategoryType.HOSPITAL, true)
        addCategoryButton(CategoryType.PHARMACY)
        addCategoryButton(CategoryType.GAS_STATION)
    }

    private fun addCategoryButton(categoryType: CategoryType, isSelected: Boolean = false) {
        val categoryButtonBinding =
            ViewCategoryButtonBinding.inflate(LayoutInflater.from(context), this, false)
                .apply {

                    root.run {
                        id = categoryType.id
                        contentDescription = context.getString(categoryType.descriptionResId)
                        updateLayoutParams<MarginLayoutParams> {
                            setMargins(5.dp.toInt())
                        }

                        setOnClickListener {
                            onClickCategoryButton(root)
                        }
                    }

                    image.setImageResource(categoryType.drawableResId)
                }

        addView(categoryButtonBinding.root)

        if (isSelected) {
            onClickCategoryButton(categoryButtonBinding.root)
        }
    }

    private fun onClickCategoryButton(clickedButton: CardView) {
        children
            .filter { childView ->
                childView.id != clickedButton.id
            }
            .forEach { childView ->
                setCategoryButtonBackground(
                    childView as CardView,
                    false
                )
            }

        setCategoryButtonBackground(clickedButton, true)
        val categoryType = CategoryType.findCategoryType(clickedButton.id)
        if (categoryType.isSuccess) {
            changeCategory?.invoke(categoryType.getOrThrow())
        }
    }

    private fun setCategoryButtonBackground(
        categoryButton: CardView,
        isSelected: Boolean
    ) {
        DrawableCompat.setTintList(
            DrawableCompat.wrap(categoryButton.background),
            ContextCompat.getColorStateList(context, getSelectedBackgroundColor(isSelected))
        )

        categoryButton.findViewById<ImageView>(R.id.image).imageTintList =
            ContextCompat.getColorStateList(context, getSelectedBackgroundColor(!isSelected))
    }

    private fun getSelectedBackgroundColor(isSelected: Boolean): Int {
        return if (isSelected) R.color.keyColor else R.color.white
    }

    fun setChangeCategoryListener(changeCategory: (CategoryType) -> Unit) {
        this.changeCategory = changeCategory
    }
}