package com.app.lurkmoarapp.di

import com.app.lurkmoarapp.JsonAdapterTest
import com.app.lurkmoarapp.PageViewModelTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    TestModule::class
])
interface TestComponent {

    fun inject(testClass: JsonAdapterTest)

    fun inject(testClass: PageViewModelTest)

}