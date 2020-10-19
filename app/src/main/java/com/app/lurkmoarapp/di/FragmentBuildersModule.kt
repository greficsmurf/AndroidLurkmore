package com.app.lurkmoarapp.di

import com.app.lurkmoarapp.ui.page.PageFragment
import com.app.lurkmoarapp.ui.page.PagePreviewBottomSheetDialog
import com.app.lurkmoarapp.ui.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule{

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributePageFragment(): PageFragment

    @ContributesAndroidInjector
    abstract fun contributePagePreviewBottomSheetDialog(): PagePreviewBottomSheetDialog

}