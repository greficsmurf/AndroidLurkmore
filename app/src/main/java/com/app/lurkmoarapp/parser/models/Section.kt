package com.app.lurkmoarapp.parser.models

import android.text.Spanned

data class Section(
    val title: String,
    val index: String,
    val text: Spanned,
    val isHeader: Boolean = false
)