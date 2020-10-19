package com.app.lurkmoarapp.vo

data class ApiResource<T>(
    val status: ResourceStatus,
    val data: T?
){
    companion object{
//        fun <T> success() =
    }
}