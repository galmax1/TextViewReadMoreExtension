package com.galmax.readmore.extension

import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.AlignmentSpan
import android.widget.TextView
import com.galmax.readmore.R

// This is special empty char that is not detectable by isBlank() function
// It is use for make word always be in one line
private const val NON_BREAKING_SPACE_CHAR: String = "\u00A0"



fun TextView.setReadMoreText(
    text: CharSequence? = null,
    showLines: Int,
    showLinesDefer: Int = 2,
    readMoreText: String = context.getString(R.string.read_more),
    readMoreStyleParam: ReadMoreSpan.StyleParam = ReadMoreSpan.StyleParam(),
    readMoreClickListener: ReadMoreSpan.ClickListener = ReadMoreSpan.ClickListener {
        maxLines = Int.MAX_VALUE
        setText(tag as CharSequence, TextView.BufferType.SPANNABLE)
    }
) {
    maxLines = if (showLines == Int.MAX_VALUE || showLinesDefer == Int.MAX_VALUE) {
        Int.MAX_VALUE
    } else {
        showLines + showLinesDefer
    }
    setText(text, TextView.BufferType.SPANNABLE)

    if (movementMethod == null) movementMethod = LinkMovementMethod()

    post {
        applyReadMoreContent(
            showLines,
            showLinesDefer,
            readMoreText,
            readMoreStyleParam,
            readMoreClickListener,
        )
    }
}

private fun TextView.applyReadMoreContent(
    showLines: Int,
    showLinesDefer: Int,
    readMoreText: String,
    readMoreStyleParam: ReadMoreSpan.StyleParam,
    readMoreClickListener: ReadMoreSpan.ClickListener
) {
    val measuredLinesCount = lineCount

    if (showLines == Int.MAX_VALUE) return // Do nothing
    if (showLines <= 0 || showLinesDefer < 0) return // Do nothing
    if (showLines + showLinesDefer >= measuredLinesCount) return // Do nothing

    // Save original full text before cut off
    tag = text

    // This is special empty char that is not detectable by isBlank() function
    // It is use for make word always be in one line
    val emptyChar = NON_BREAKING_SPACE_CHAR
    val nonBreakingReadMoreText = readMoreText.split(" ").joinToString(emptyChar)
    val truncatedMarkText = "...${emptyChar}"

    val lastLineIndex = showLines - 1
    val lineEndIndex = layout.getLineEnd(lastLineIndex)
    val lineStartIndex = layout.getLineStart(lastLineIndex)

    // Try to make read more text fit last line
    var cutTextLastIndex = lineEndIndex
    if (layout.getLineWidth(lastLineIndex) > layout.width / 2) {
        cutTextLastIndex = lineEndIndex - ((lineEndIndex - lineStartIndex) / 2)
    }

    val cutText: CharSequence = text.subSequence(0, cutTextLastIndex)

    val spannableStringBuilder = SpannableStringBuilder(cutText.trimEnd())
    spannableStringBuilder.append(truncatedMarkText)

    // Set clickable span for "read more" action
    val readMoreTextStart = spannableStringBuilder.lastIndex + 1
    spannableStringBuilder.append(nonBreakingReadMoreText)
    val readMoreTextEnd = spannableStringBuilder.lastIndex + 1

    spannableStringBuilder.setSpan(
        ReadMoreSpan(readMoreStyleParam, readMoreClickListener),
        readMoreTextStart, readMoreTextEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableStringBuilder.setSpan(
        AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
        readMoreTextStart, readMoreTextEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    setText(spannableStringBuilder, TextView.BufferType.SPANNABLE)
}