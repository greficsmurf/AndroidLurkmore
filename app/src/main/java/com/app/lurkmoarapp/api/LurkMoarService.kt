package com.app.lurkmoarapp.api

import com.app.lurkmoarapp.apimodels.ApiPageContainer
import com.app.lurkmoarapp.apimodels.ApiSearchResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface LurkMoarService{

    @GET("api.php?action=opensearch&format=json")
    suspend fun getSearchResult(@Query("search") search: String): ApiSearchResult

    @GET("api.php?action=parse&format=json&prop=wikitext|revid|sections&redirects")
    suspend fun getPage(@Query("page") page: String): ApiPageContainer

    @GET("api.php?action=parse&format=json&prop=wikitext|revid|sections&redirects&section=0")
    suspend fun getPageHeader(@Query("page") page: String): ApiPageContainer
}