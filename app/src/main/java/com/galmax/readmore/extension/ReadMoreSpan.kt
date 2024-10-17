package com.galmax.readmore.extension

import android.graphics.Color
import android.graphics.Typeface
import android.os.SystemClock
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt

class ReadMoreSpan(
    private val styleParam: StyleParam,
    private val listener: ClickListener
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
        ds.color = styleParam.textColor
        ds.typeface = Typeface.create(Typeface.DEFAULT, styleParam.textStyle)
    }

    private var lastTimeClicked: Long = 0

    override fun onClick(widget: View) {
        // Prevent double click on span
        if (SystemClock.elapsedRealtime() - lastTimeClicked < 200) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()

        listener.onReadMoreClick(widget as TextView)
    }

    fun interface ClickListener {
        fun onReadMoreClick(view: TextView)
    }

    data class StyleParam(
        @ColorInt
        val textColor: Int = Color.BLUE,
        val textStyle: Int = Typeface.BOLD,
    )
}