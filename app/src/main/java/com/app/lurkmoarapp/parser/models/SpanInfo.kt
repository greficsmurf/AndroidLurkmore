package com.app.lurkmoarapp.parser.models

import android.text.Spannable
import android.text.style.CharacterStyle

data class SpanInfo(
    val span: Any,
    val range: IntRange,

    val flags: Int = Spannable.SPAN_INCLUSIVE_EXCLUSIVE
)