package com.app.lurkmoarapp.parser

import android.text.SpannableString
import com.app.lurkmoarapp.domain.PageSection
import com.app.lurkmoarapp.parser.models.Section
import com.app.lurkmoarapp.parser.models.SpecialCharGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class LurkParser(
    private val txt: String,
    private val pageSectionsInfo: List<PageSection>,
    specialCharGroups: List<SpecialCharGroup>

    ) : Parser(
    txt,
    specialCharGroups
) {
    fun getHeader(): Section {

        if(txt.isEmpty())
            return Section(
                "",
                "-1",
                SpannableString("")
            )
        val currentSectionTxt = txt.substring(IntRange(0,
            pageSectionsInfo.getOrNull(0)?.byteOffset?.toInt()?.minus(1) ?: 1))

        val spannedTxt = toSpannableHtml(toHtml(currentSectionTxt))

        return Section(
            "",
            "0",
            spannedTxt,
            true
        )
    }

    suspend fun getSectionFlow(): Flow<List<Section>> = flow {
        var currentSectionInfo = PageSection(
            1,
            2,
            "",
            "-1",
            "-1",
            "",
            0
        )
        var startPos = -1

        withContext(Dispatchers.Default){
            val sections = mutableListOf<Section>().apply {
                add(getHeader())
            }

            for((index, sectionInfo) in pageSectionsInfo.withIndex()){
                if(sectionInfo.byteOffset == -1L ||
                    sectionInfo.byteOffset > txt.length ||
                    sectionInfo.byteOffset == currentSectionInfo.byteOffset)
                    continue

                if(startPos == -1)
                {
                    startPos = sectionInfo.byteOffset.toInt()
                    continue
                }

                val currentSectionTxt = txt.substring(IntRange(startPos,
                    sectionInfo.byteOffset.toInt() - 1))

                val spannedTxt = toSpannableHtml(toHtml(currentSectionTxt))

                sections.add(
                    Section(
                        currentSectionInfo.line,
                        currentSectionInfo.number,
                        spannedTxt
                    )
                )

                if(index == pageSectionsInfo.lastIndex){
                    val lastSectionTxt = txt.substring(IntRange(sectionInfo.byteOffset.toInt(),
                        txt.lastIndex))
                    sections.add(
                        Section(
                            sectionInfo.line,
                            sectionInfo.number,
                            toSpannableHtml(toHtml(lastSectionTxt))
                        )
                    )
                }

                emit(sections.toList())

                startPos = sectionInfo.byteOffset.toInt()
                currentSectionInfo = sectionInfo
            }
        }
    }
}