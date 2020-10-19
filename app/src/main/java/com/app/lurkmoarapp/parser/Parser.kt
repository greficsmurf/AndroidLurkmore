package com.app.lurkmoarapp.parser

import android.content.Intent
import android.net.Uri
import android.text.*
import android.text.style.*
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.navigation.findNavController
import com.app.lurkmoarapp.R
import com.app.lurkmoarapp.parser.models.SectionDividerCharGroup
import com.app.lurkmoarapp.parser.models.SpanInfo
import com.app.lurkmoarapp.parser.models.SpecialCharGroup
import com.app.lurkmoarapp.ui.page.PageFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.math.abs
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

open class Parser @Inject constructor(
    private val txt: String,
    private val specialCharGroups: List<SpecialCharGroup>
){
    fun toHtml(txt: String? = null): String{
        var tempTxt = removeExtraParts(txt ?: this.txt)

        do {
            for(charGroup in specialCharGroups){
                tempTxt = charGroup.wrapByHtml(tempTxt)
            }
        }while (isSpecialCharsLeft(tempTxt))

        return tempTxt.replace("[\\{\\}]".toRegex(), "")
    }

    private fun isSpecialCharsLeft(txt: String): Boolean{
        for(specialCharGroup in specialCharGroups){
            if(specialCharGroup.findFirst(txt).isNotBlank())
                return true
        }
        return false
    }

    private fun cutHeader(txt: String): String{
        return txt
    }

    fun toSpannableHtml(htmlString: String? = null, isHeader: Boolean = false): Spanned{
        val htmlStr = htmlString ?: if(isHeader) toHtml(cutHeader(txt)) else toHtml()

        val htmlSpannedStr = HtmlCompat.fromHtml(
            htmlStr,
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            null){
                opening,
                tag,
                output,
                xmlReader ->

            if(opening){
                specialCharGroups.find { span ->
                    span.htmlTagName == tag
                }?.let { charGroup ->
                    try {
                        output.setSpan(charGroup.getSpans("").first().span, output.length, output.length, Spannable.SPAN_MARK_MARK)
                    }catch (e: Exception){}
                }
            }
            else{
                specialCharGroups.find { span ->
                    span.htmlTagName == tag
                }?.let { charGroup ->
                    val span = charGroup.getSpans("").first().span
                    val spanClass = span.javaClass.let {
                        if(it.superclass == null || it.superclass.isInstance(JvmType.Object::class.java)){
                            it
                        }
                        else
                            it.superclass
                    }
                    val paragraphStartEndChar = '\n'

                    val lastSpan = output.getSpans(0, output.length, spanClass).last()
                    val start = output.getSpanStart(lastSpan)
                    output.removeSpan(lastSpan)
                    val txtRange = IntRange(start, output.lastIndex)
                    val spans = charGroup.getSpans(output.substring(txtRange))

                    val offsetList = mutableListOf<Int>()
                    fun getPosOffset(pos: Int) = offsetList.count {
                        it <= pos
                    }

                    for ((index, spanItem) in spans.withIndex()){
                        val startPoint = start + spanItem.range.first
                        val endPoint = start + spanItem.range.last

                        if(span is ParagraphStyle){
                            output.insert(startPoint + getPosOffset(startPoint), "$paragraphStartEndChar")
                            offsetList.add(startPoint)
                            output.insert(endPoint + getPosOffset(endPoint), "$paragraphStartEndChar")

                            offsetList.add(endPoint)

                            val paragraphStart = startPoint + getPosOffset(startPoint)
                            val paragraphEnd = endPoint + getPosOffset(endPoint)

                            if(index == spans.indexOfLast { it.span is ParagraphStyle })
                                output.insert(endPoint + getPosOffset(endPoint), " ")

                            if(output[paragraphStart-1] == '\n' && output[paragraphEnd-1] == '\n')
                                output.setSpan(spanItem.span, paragraphStart, paragraphEnd, spanItem.flags)

                        }else
                            output.setSpan(spanItem.span, startPoint, endPoint, spanItem.flags)
                    }
                }
            }
        }
        return htmlSpannedStr
    }

    private fun removeExtraParts(txt: String) = txt.replace("\n", "<br>")
}