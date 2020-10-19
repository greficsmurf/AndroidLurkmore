package com.app.lurkmoarapp.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.app.lurkmoarapp.api.LurkMoarService
import com.app.lurkmoarapp.apimodels.ApiPageContainer
import com.app.lurkmoarapp.db.dao.DbPageDao
import com.app.lurkmoarapp.db.dao.DbPageSectionDao
import com.app.lurkmoarapp.db.model.DbPage
import com.app.lurkmoarapp.domain.Page
import com.app.lurkmoarapp.domain.PageContainer
import com.app.lurkmoarapp.domain.mapper.asDbModel
import com.app.lurkmoarapp.domain.mapper.asDomainModel
import com.app.lurkmoarapp.vo.NetworkBoundResource
import com.app.lurkmoarapp.vo.NetworkResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PageRepo @Inject constructor(
    private val lurkApi: LurkMoarService,
    private val dbPageDao: DbPageDao,
    private val dbPageSectionDao: DbPageSectionDao
) {


    fun getPageResource(page: String) = object : NetworkBoundResource<ApiPageContainer, DbPage, Page>(){
        override suspend fun shouldFetchApi(data: DbPage?) = data == null

        override suspend fun fetchApi() = lurkApi.getPage(page)

        override suspend fun fetchDb() = dbPageDao.getByTitle(page)

        override suspend fun onSave(output: Page) {
            dbPageDao.insert(output.asDbModel())
        }

        override suspend fun processApiOutput(res: ApiPageContainer) = res.asDomainModel().page

        override suspend fun processDbOutput(res: DbPage) = res.asDomainModel()
    }.asFlow()

    suspend fun getPageHeader(page:String): PageContainer{
        return lurkApi.getPageHeader(page).asDomainModel()
    }

    fun getPageHeaderResource(page: String) = object : NetworkResource<ApiPageContainer, PageContainer>(){
        override suspend fun fetch() = lurkApi.getPageHeader(page)

        override suspend fun processOutput(req: ApiPageContainer) = req.asDomainModel()
    }.asFlow()
}