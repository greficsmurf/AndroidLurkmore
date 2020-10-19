package com.app.lurkmoarapp.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.WindowInsets
import androidx.appcompat.widget.AppCompatEditText
import com.app.lurkmoarapp.R
import timber.log.Timber

open class CleanableEditText : AppCompatEditText, TextWatcher {
    constructor(context: Context)
            : super(context)
    constructor(context: Context, attributeSet: AttributeSet?)
            : super(context, attributeSet, android.R.attr.editTextStyle)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr)

    private var isClearIconShown = false
    private var clearIconDrawable = context.getDrawable(R.drawable.ic_clear_24px)

    init {
        background = null
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        setClearIcon()
    }

    private fun setClearIcon(){
        if(text.isNullOrEmpty())
            removeClearIcon()
        else
            showClearIcon()
    }

    override fun onAttachedToWindow() {
        restoreIcon()
        super.onAttachedToWindow()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(isClearIconTouched(event) && event?.action == MotionEvent.ACTION_UP){
            removeClearIcon()

            text = null
            event.action = MotionEvent.ACTION_CANCEL
        }

        return super.onTouchEvent(event)
    }

    private fun isClearIconTouched(event: MotionEvent?): Boolean{
        val x = event?.x

        return if(x != null){
            x >= width - compoundPaddingRight
        }else
            false
    }
    private fun restoreIcon(){
        resetIconStatus()
        setClearIcon()
    }
    private fun resetIconStatus(){
        isClearIconShown = false
    }

    private fun showClearIcon(){
        if(hasFocus() && !isClearIconShown){
            setDrawableRight(clearIconDrawable)
            isClearIconShown = true
        }
    }

    private fun removeClearIcon(){
        if(isClearIconShown)
        {
            setDrawableRight(null)
            isClearIconShown = false
        }
    }

    private fun setDrawableRight(drawable: Drawable?){
        setCompoundDrawablesWithIntrinsicBounds(
            compoundDrawables[0],
            compoundDrawables[1],
            drawable,
            compoundDrawables[3]
        )
    }

}