package com.app.lurkmoarapp.vo

import com.app.lurkmoarapp.markers.DatabaseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber

abstract class DatabaseResource <ReqType, OutType> {

    private val result: Flow<Resource<out OutType>> = flow {
        emit(Resource.loading(null))
        try{
            fetch().collect{ dbRes ->
                emit(
                    Resource.success(processOutput(dbRes))
                )
            }
        }catch (e: Exception){
            emit(Resource.failed<OutType>(e.message ?: "error"))
            Timber.e("DatabaseResource error: %s", e.message)
        }
    }

    fun asFlow() = result

    protected abstract suspend fun fetch() : Flow<ReqType>
    protected abstract suspend fun processOutput(req: ReqType) : OutType
}