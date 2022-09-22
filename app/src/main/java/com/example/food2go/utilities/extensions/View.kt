package com.trackerteer.taskmanagement.utilities.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.example.food2go.utilities.`interface`.SafeClickListener

fun View.fadesOut() {
    animate().alpha(0.0f)
}

fun View.fadesIn() {
    animate().alpha(1.0f)
}

fun View.fadesOut(duration: Long) {
    animate().alpha(0.0f).duration = duration
}

fun View.fadesIn(duration: Long) {
    animate().alpha(1.0f).duration = duration
}

fun View.animateByHeight() {
    animate().translationY(this.height.toFloat())
}

fun View.animateByWidth() {
    animate().translationY(this.width.toFloat())
}

fun View.translateY(duration: Long) {
    animate().translationY(1f).duration = duration
}

fun View.translateX() {
    animate().translationY(1f)
}

fun View.gone(){
    visibility = View.GONE
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun focusView(view: View) {
    view.isFocusableInTouchMode = true
    view.requestFocus()
    view.isFocusableInTouchMode = false
}

fun fadeIn(view: View) {
    view.alpha = 0.0f
    view.visibility = View.VISIBLE
    view.animate()
        .alpha(1f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.VISIBLE
            }
        })
}

fun fadeOut(view: View) {
    view.animate()
        .alpha(0.0f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit){

    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}