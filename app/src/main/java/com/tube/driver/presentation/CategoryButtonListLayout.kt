package com.tube.driver.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tube.driver.R
import com.tube.driver.databinding.ViewCategoryButtonBinding
import com.tube.driver.domain.CategoryType
import com.tube.driver.dp

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
        val categoryButton =
            ViewCategoryButtonBinding.inflate(LayoutInflater.from(context), this, false)
                .root
                .apply {
                    id = categoryType.id
                    backgroundTintList = ContextCompat.getColorStateList(
                        context,
                        getSelectedBackgroundColor(isSelected)
                    )
                    setImageResource(categoryType.drawableResId)
                    contentDescription = context.getString(categoryType.descriptionResId)
                    updateLayoutParams<MarginLayoutParams> {
                        setMargins(5.dp.toInt())
                    }

                    setOnClickListener { clickedButton ->
                        onClickCategoryButton(clickedButton as FloatingActionButton)
                    }
                }

        addView(categoryButton)
    }

    private fun onClickCategoryButton(clickedButton: FloatingActionButton) {
        children
            .filter { childView ->
                childView.id != clickedButton.id
            }
            .forEach { childView ->
                setCategoryButtonBackground(
                    childView as FloatingActionButton,
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
        categoryButton: FloatingActionButton,
        isSelected: Boolean
    ) {
        DrawableCompat.setTintList(
            DrawableCompat.wrap(categoryButton.background),
            ContextCompat.getColorStateList(context, getSelectedBackgroundColor(isSelected))
        )
    }

    private fun getSelectedBackgroundColor(isSelected: Boolean): Int {
        return if (isSelected) R.color.ff1ea1f3 else R.color.white
    }

    fun setChangeCategoryListener(changeCategory: (CategoryType) -> Unit) {
        this.changeCategory = changeCategory
    }
}