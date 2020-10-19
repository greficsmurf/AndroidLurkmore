package com.app.lurkmoarapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.lurkmoarapp.ui.page.PageViewModel
import com.app.lurkmoarapp.ui.search.SearchViewModel
import com.app.lurkmoarapp.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Suppress("unused")
@Module
abstract class ViewModelModule{

    @Binds
    @IntoSet
    abstract fun bindSearchViewModel(vm: SearchViewModel) : ViewModel

    @Binds
    @IntoSet
    abstract fun bindPageViewModel(vm: PageViewModel) : ViewModel

    @Binds
    abstract fun bindViewModelFactory(vmf: ViewModelFactory) : ViewModelProvider.Factory

}