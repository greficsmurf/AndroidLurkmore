package com.app.lurkmoarapp.vo

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception

abstract class NetworkResource <ReqType, OutType> {

    private val result: Flow<Resource<out OutType>> = flow{
        emit(Resource.loading(null))
        try{
            val res = processOutput(fetch())
            emit(Resource.success(res))
            onSave(res)
        }catch (e: Exception){
            Timber.e("NetworkResource error %s", e.message)
            emit(Resource.failed<OutType>(e.message ?: "error"))
        }
    }

    fun asFlow() = result

    protected abstract suspend fun fetch() : ReqType
    protected abstract suspend fun processOutput(req: ReqType): OutType

    protected open suspend fun onSave(out: OutType){}
}