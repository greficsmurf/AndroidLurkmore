package com.app.lurkmoarapp

import android.util.Log
import com.app.lurkmoarapp.api.LurkMoarService
import com.app.lurkmoarapp.di.DaggerTestComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class JsonAdapterTest {

    @Inject
    lateinit var api: LurkMoarService

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun before(){
        DaggerTestComponent.builder().build().inject(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
    @Test
    fun testPageParseApi() = runBlocking {
        val test = api.getPage("TeX")
        println(test)
    }
    @Test
    fun testSearchResultApi() = runBlocking {
        val test = api.getSearchResult("te")
        test.searchResults.forEach {
            println(it)
        }
    }
}