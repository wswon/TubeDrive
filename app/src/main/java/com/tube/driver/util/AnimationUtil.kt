package com.tube.driver.util

import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

object AnimationUtil {
    fun fadeIn(view: View) {
        SpringAnimation(view, SpringAnimation.ALPHA)
            .setSpring(
                SpringForce().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
            .setStartValue(0.0f)
            .animateToFinalPosition(1.0f)
    }

    fun fadeOut(view: View) {
        SpringAnimation(view, SpringAnimation.ALPHA)
            .setSpring(
                SpringForce().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
            .setStartValue(1.0f)
            .animateToFinalPosition(0.0f)
    }
}