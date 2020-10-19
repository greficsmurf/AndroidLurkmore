package com.app.lurkmoarapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import com.app.lurkmoarapp.repo.PageRepo
import com.app.lurkmoarapp.ui.page.PageViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class PageViewModelTest  {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var vm: PageViewModel

    private val mainThread = newSingleThreadContext("UI thread")

    @Mock
    lateinit var pageRepo: PageRepo

    @Before
    fun before(){
        Dispatchers.setMain(mainThread)
        MockitoAnnotations.initMocks(this)
        vm = PageViewModel(pageRepo)

    }

    @After
    fun after(){
        mainThread.close()
        Dispatchers.resetMain()
    }

}