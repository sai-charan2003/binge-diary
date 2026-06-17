package com.charan.bingediary.di

import com.charan.bingediary.data.local.BingeDiaryDatabase
import com.charan.bingediary.data.local.dao.ReviewDao
import com.charan.bingediary.data.local.dao.UserMediaDao
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
@ComponentScan("com.charan.bingediary")
class AppModule {
    @Single
    fun getUserMediaDao(
        database: BingeDiaryDatabase
    ) : UserMediaDao {
        return database.getUserMediaDao()
    }

    @Single
    fun getReviewDao(
        database: BingeDiaryDatabase
    ) : ReviewDao {
        return database.getReviewDao()
    }
}

@KoinApplication(modules = [AppModule::class])
class App
