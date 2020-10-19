package com.app.lurkmoarapp.di

import com.app.lurkmoarapp.api.LurkMoarService
import com.app.lurkmoarapp.apimodels.LurkApiAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class TestModule {

    @Singleton
    @Provides
    fun provideLurkMoarService(): LurkMoarService {
        val moshi = Moshi.Builder()
            .add(LurkApiAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://lurkmore.to/")
            .build()
            .create(LurkMoarService::class.java)
    }



}