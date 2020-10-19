package com.app.lurkmoarapp.vo

data class Resource<T>(
    val status: ResourceStatus,
    val data: T?,
    val message: String = ""
){
    companion object{
        fun <T> success(data: T?) = Resource(ResourceStatus.SUCCESS, data)
        fun <T> loading(data: T?) = Resource(ResourceStatus.LOADING, data)
        fun <T> failed(msg: String) = Resource(ResourceStatus.FAILED, null, message = msg)
    }
}