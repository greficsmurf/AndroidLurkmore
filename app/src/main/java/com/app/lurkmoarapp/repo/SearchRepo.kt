package com.app.lurkmoarapp.repo

import androidx.lifecycle.liveData
import com.app.lurkmoarapp.api.LurkMoarService
import com.app.lurkmoarapp.apimodels.ApiSearchResult
import com.app.lurkmoarapp.db.dao.DbSearchItemDao
import com.app.lurkmoarapp.db.model.DbSearchItem
import com.app.lurkmoarapp.domain.SearchItem
import com.app.lurkmoarapp.domain.SearchResult
import com.app.lurkmoarapp.domain.mapper.asDbModel
import com.app.lurkmoarapp.domain.mapper.asDomainModel
import com.app.lurkmoarapp.vo.DatabaseResource
import com.app.lurkmoarapp.vo.NetworkResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepo @Inject constructor(
    private val lurkApi: LurkMoarService,
    private val searchItemDao: DbSearchItemDao
) {

//    suspend fun getSearchResults(searchStr: String) = lurkApi.getSearchResult(searchStr).asDomainModel()
//
//    fun getLastSearchItems(count: Int) = searchItemDao.getLastItems(count).map {
//        it.asDomainModel().apply {
//            searchResults.map { searchItem ->  searchItem.isHistory = true }
//        }
//    }

    fun getLastSearchItemsResource(count: Int) = object : DatabaseResource<List<DbSearchItem>, SearchResult>() {
        override suspend fun fetch(): Flow<List<DbSearchItem>> = searchItemDao.getLastItems(count)
        override suspend fun processOutput(req: List<DbSearchItem>) = req.asDomainModel().apply { searchResults.map {
            it.isHistory = true
        } }
    }.asFlow()

    fun getSearchResultsResource(searchStr: String) = object : NetworkResource<ApiSearchResult, SearchResult>(){
        override suspend fun fetch() = lurkApi.getSearchResult(searchStr)
        override suspend fun processOutput(req: ApiSearchResult) = req.asDomainModel()
        override suspend fun onSave(out: SearchResult) {
            super.onSave(out)
            insertSearchItem(
                SearchItem(
                    searchStr
                )
            )
        }
    }.asFlow()

    suspend fun insertSearchItem(searchItem: SearchItem){
        searchItemDao.insert(searchItem.asDbModel())
    }

    suspend fun deleteAllHistory(){
        searchItemDao.deleteAll()
    }
}