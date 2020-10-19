package com.app.lurkmoarapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val creators: Set<@JvmSuppressWildcards ViewModel>) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val viewModel = creators.firstOrNull {
            modelClass.isAssignableFrom(it::class.java)
        }?: throw Exception("Unrecognized viewModel is passed")

        @Suppress("UNCHECKED_CAST")
        return viewModel as T
    }
}