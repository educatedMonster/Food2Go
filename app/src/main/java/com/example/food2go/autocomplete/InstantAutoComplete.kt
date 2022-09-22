package com.example.food2go.autocomplete

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import java.util.*

class InstantAutoComplete : AppCompatAutoCompleteTextView {
    private var mShowUnfilteredListWhenClicked = false
    private val MAX_CLICK_DURATION = 300
    private val MAX_CLICK_DISTANCE = 5
    private var startClickTime: Long = 0
    private var x1 = 0f
    private var y1 = 0f
    private var x2 = 0f
    private var y2 = 0f
    private var dx = 0f
    private var dy = 0f

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr) {
    }

    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && filter != null) {
            if (mShowUnfilteredListWhenClicked) {
                performFiltering("", 0)
            } else if (filter == null) {
                showDropDown()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startClickTime = Calendar.getInstance().timeInMillis
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                val clickDuration = Calendar.getInstance().timeInMillis - startClickTime
                x2 = event.x
                y2 = event.y
                dx = x2 - x1
                dy = y2 - y1
                if (clickDuration < MAX_CLICK_DURATION && dx < MAX_CLICK_DISTANCE && dy < MAX_CLICK_DISTANCE) {
                    if (mShowUnfilteredListWhenClicked) {
                        showDropDown()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun setShowUnfilteredListWhenClicked(show: Boolean) {
        mShowUnfilteredListWhenClicked = show
    }
}