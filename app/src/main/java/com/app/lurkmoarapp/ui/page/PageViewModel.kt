package com.app.lurkmoarapp.ui.page

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.*
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.text.toSpanned
import androidx.lifecycle.*
import com.app.lurkmoarapp.R
import com.app.lurkmoarapp.domain.models.ContentTableItem
import com.app.lurkmoarapp.liveevent.LiveEvent
import com.app.lurkmoarapp.parser.LurkParser
import com.app.lurkmoarapp.parser.Parser
import com.app.lurkmoarapp.parser.models.SpanInfo
import com.app.lurkmoarapp.parser.models.SpecialCharGroup
import com.app.lurkmoarapp.repo.PageRepo
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class PageViewModel @Inject constructor(
    private val repo: PageRepo
) : ViewModel(){
    @RequiresApi(Build.VERSION_CODES.P)
    private val specialCharGroups = listOf(
        SpecialCharGroup("[[Файл:", "]]", htmlTagName = "removetag"){ txt ->
            listOf(
                SpanInfo(
                    ScaleXSpan(0F),
                    IntRange(0, txt.length),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            )
        },

        SpecialCharGroup("[[", "]]", htmlTagName = "internallink"){ txt ->
            val list = txt.split("|")
            var name = txt
            var clickRange = IntRange(0, txt.length)
            var removeRange = IntRange(0,0)

            if(list.size > 1){
                removeRange = IntRange(0, list[0].length + 1)
                clickRange = IntRange(list[0].length + 1, txt.length)

                name = list[0]
            }

            listOf(
                SpanInfo(
                    object : ClickableSpan(){
                        override fun onClick(widget: View) {
                            try{
                                _shortInfoPageName.postValue(name)
                            }catch (e: Exception){
                                Snackbar.make(widget, e.message ?: "Error", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    },
                    clickRange,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                ),
                SpanInfo(
                    object : AbsoluteSizeSpan(0, true){},
                    removeRange,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            )
        },

        SpecialCharGroup("[", "]", htmlTagName = "externallink"){ txt ->
            val list = txt.split(" ")
            var clickRange = IntRange(0, txt.length)
            var removeRange = IntRange(0,0)
            if(list.size > 1){
                removeRange = IntRange(0, list[0].length)
                clickRange = IntRange(list[0].length, txt.length)
            }

            listOf(
                SpanInfo(
                    object : ClickableSpan(){
                        override fun onClick(widget: View) {
                            try {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(txt.substring(removeRange)))
                                widget.context.startActivity(browserIntent)
                            }catch (e: Exception){
                                Snackbar.make(widget, widget.context.getString(R.string.open_link_error) , Snackbar.LENGTH_SHORT).show()
                            }
                        }

                    },
                    clickRange
                ),

                SpanInfo(
                    object : AbsoluteSizeSpan(0, true){},
                    removeRange
                )
            )
        },

        SpecialCharGroup("{{Q|", "}}",
            htmlTagName = "blockquotecustom"){txt ->

            val list = txt.split("|")

            if(list.size == 2){
                return@SpecialCharGroup listOf(
                    SpanInfo(
                        QuoteSpan(Color.LTGRAY, 5, 20),
                        IntRange(0, list[0].length),
                        Spanned.SPAN_PARAGRAPH
                    ),
                    SpanInfo(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
                        IntRange(list[0].length, txt.length),
                        Spanned.SPAN_PARAGRAPH
                    ),
                    SpanInfo(
                        ScaleXSpan(0F),
                        IntRange(txt.indexOf('|'), txt.indexOf('|') + 1)
                    )
                )
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                listOf(
                    SpanInfo(
                        QuoteSpan(Color.LTGRAY, 5, 20),
                        IntRange(0, txt.length),
                        Spanned.SPAN_PARAGRAPH
                    )
                )
            else
                listOf(
                    SpanInfo(
                        QuoteSpan(Color.LTGRAY),
                        IntRange(0, txt.length),
                        Spanned.SPAN_PARAGRAPH
                    )
                )
        },
        SpecialCharGroup("{{цитата|", "}}",
            htmlTagName = "blockquotecustom"),
        SpecialCharGroup("{{Цитата|pre=1|", "}}",
            htmlTagName = "blockquotecustom"),
        SpecialCharGroup("{{Цитата|", "}}",
            htmlTagName = "blockquotecustom"),

        SpecialCharGroup("====", "====",
            htmlTagName = "h5"),

        SpecialCharGroup("===", "===",
            htmlTagName = "h4"),

        SpecialCharGroup("==", "==",
            htmlTagList = listOf("h3", "u")),

        SpecialCharGroup("'''",
            htmlTagName = "strong"),

        SpecialCharGroup("''", htmlTagName = "i")
    )

    private val job = Job()
    private val coroutinesScope = CoroutineScope(job + Dispatchers.Main)

    private val _pageName = MutableLiveData<String>()
    val pageName: LiveData<String>
        get() = _pageName

    val pageResource = _pageName.switchMap { pageName ->
        repo.getPageResource(pageName).asLiveData()
    }

    private val _currentSectionNumber = MutableLiveData<String>()
    val currentSectionNumber: LiveData<String>
        get() = _currentSectionNumber

    private val _tableOfContentsList = MutableLiveData<List<ContentTableItem>>()
    val tableOfContentTableItem: LiveData<List<ContentTableItem>>
        get() = _tableOfContentsList

    private val _shortInfoPageName = LiveEvent<String>()
    val shortInfoPageName: LiveData<String>
        get() = _shortInfoPageName

    private val _clickedPagePreviewTitle = MutableLiveData<String>()
    val clickedPagePreviewTitle: LiveData<String>
        get() = _clickedPagePreviewTitle

    val clickedPagePreviewResource = _shortInfoPageName.switchMap {pageName ->
        repo.getPageHeaderResource(pageName).asLiveData()
    }

    val clickedPagePreviewText = clickedPagePreviewResource.switchMap { pagePreviewResource ->
        val pageContainer = pagePreviewResource.data
        if(pageContainer != null)
            liveData(Dispatchers.Default) {
                val wikiText = pageContainer.page.parsedWikiText?.text ?: ""
                val parsedText = Parser(wikiText, specialCharGroups).toSpannableHtml(isHeader = true)
                _clickedPagePreviewTitle.postValue(pageContainer.page.title)
                emit(parsedText)
            }
        else
            liveData(Dispatchers.Default) {
                _clickedPagePreviewTitle.postValue("")
                emit(SpannableString("").toSpanned())
            }
    }

    val parsedWikiTextSections = pageResource.switchMap{ pageRes ->
        val page = pageRes.data
        if(page != null)
            liveData(Dispatchers.Default) {
                val txt = page.parsedWikiText?.text ?: ""
                val sections = page.sections
                val parser = LurkParser(txt, sections, specialCharGroups)
                _tableOfContentsList.postValue(
                    sections.filter {pageSection ->
                        pageSection.byteOffset != -1L
                    }.map { pageSection ->
                        ContentTableItem(
                            pageSection.line,
                            pageSection.number
                        )
                    }
                )
                parser.getSectionFlow().collect{
                    emit(it)
                }
            }
        else
            liveData {

            }
    }

    init {
        Timber.i("created")
    }

    fun setPageName(name: String){
        if(_pageName.value != name)
        {
            _pageName.value = name
        }
    }
    fun setCurrentSection(sectionNumber: String){
        _currentSectionNumber.postValue(sectionNumber)
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}