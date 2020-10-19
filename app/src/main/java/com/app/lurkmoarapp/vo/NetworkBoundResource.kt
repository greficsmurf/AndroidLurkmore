package com.app.lurkmoarapp.vo

import com.app.lurkmoarapp.db.model.DbPage
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.lang.Exception

/*
* Fetches data from database and api
* */
abstract class NetworkBoundResource<ResApiType, ResDbType, OutType> {

    private val result = flow<Resource<out OutType>> {
        emit(Resource.loading(null))
        coroutineScope {
            try{
                fetchDb().collect { dbRes ->
                    emit(Resource.loading(null))

                    if(shouldFetchApi(dbRes)) {
                        val data = processApiOutput(fetchApi())
                        emit(Resource.success(data))
                        onSave(data)

                        Timber.d("Fetched from network")
                    }
                    else{
                        dbRes?.let {
                            emit(Resource.success(processDbOutput(it)))
                        }

                        Timber.d("Fetched from database")
                    }

                    cancel()
                }
            }catch (e: Exception){
                emit(Resource.failed<OutType>(e.message ?: ""))
                Timber.e("NetworkBoundResource %s", e.message)
            }
        }
    }

    fun asFlow() = result

    protected abstract suspend fun shouldFetchApi(data: ResDbType?): Boolean
    protected abstract suspend fun fetchApi(): ResApiType
    protected abstract suspend fun fetchDb(): Flow<ResDbType?>
    protected abstract suspend fun onSave(output: OutType)
    protected abstract suspend fun processApiOutput(res: ResApiType): OutType
    protected abstract suspend fun processDbOutput(res: ResDbType): OutType
}