package com.app.lurkmoarapp.di

import android.app.Application
import androidx.room.Room
import com.app.lurkmoarapp.api.LurkMoarService
import com.app.lurkmoarapp.apimodels.LurkApiAdapter
import com.app.lurkmoarapp.db.MyRoomDatabase
import com.app.lurkmoarapp.db.dao.DbPageDao
import com.app.lurkmoarapp.db.dao.DbSearchItemDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule{

    @Singleton
    @Provides
    fun provideLurkMoarService(): LurkMoarService{
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

    @Singleton
    @Provides
    fun provideRoomDatabase(app: Application): MyRoomDatabase{
        return Room.databaseBuilder(
            app,
            MyRoomDatabase::class.java, "mydb"
        ).build()
    }

    @Singleton
    @Provides
    fun provideDbPageDao(db: MyRoomDatabase) = db.pageDao()

    @Singleton
    @Provides
    fun provideDbSearchItemDao(db: MyRoomDatabase) = db.searchItemDao()

    @Singleton
    @Provides
    fun provideDbPageSectionDao(db: MyRoomDatabase) = db.pageSectionDao()
}