package com.tube.driver.presentation.place.view

import android.content.Context
import android.os.Parcelable
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
import kotlinx.parcelize.Parcelize

class CategoryButtonListLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private var selectedCategory: CategoryType = CategoryType.HOSPITAL
    private var changeCategory: ((CategoryType) -> Unit)? = null

    init {
        addCategoryButton(CategoryType.HOSPITAL, selectedCategory == CategoryType.HOSPITAL)
        addCategoryButton(CategoryType.PHARMACY, selectedCategory == CategoryType.PHARMACY)
        addCategoryButton(CategoryType.GAS_STATION, selectedCategory == CategoryType.GAS_STATION)
    }

    private fun makeCategoryButtonBinding(categoryType: CategoryType): ViewCategoryButtonBinding =
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

    private fun addCategoryButton(categoryType: CategoryType, isSelected: Boolean = false) {
        val categoryButtonBinding =
            makeCategoryButtonBinding(categoryType)

        addView(categoryButtonBinding.root)

        if (isSelected) {
            onClickCategoryButton(categoryButtonBinding.root)
        }
    }

    private fun onClickCategoryButton(clickedButton: CardView) {
        toggleSelected(clickedButton)
        val categoryType = CategoryType.findCategoryType(clickedButton.id)
        if (categoryType.isSuccess) {
            val clickedCategory = categoryType.getOrThrow()
            if (selectedCategory != clickedCategory) {
                selectedCategory = clickedCategory
                changeCategory?.invoke(clickedCategory)
            }
        }
    }

    private fun toggleSelected(selectedView: CardView) {
        children
            .filter { childView ->
                childView.id != selectedView.id
            }
            .forEach { childView ->
                setCategoryButtonBackground(
                    childView as CardView,
                    false
                )
            }
        setCategoryButtonBackground(selectedView, true)
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

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState(), selectedCategory)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as? SavedState
        savedState?.selectedCategoryType?.let {
            selectedCategory = it
        }

        val view = children.find { it.id == selectedCategory.id }
        if (view is CardView) {
            toggleSelected(view)
        }
        super.onRestoreInstanceState(savedState?.superState)
    }

    @Parcelize
    class SavedState(
        val superState: Parcelable?,
        var selectedCategoryType: CategoryType
    ) : Parcelable
}