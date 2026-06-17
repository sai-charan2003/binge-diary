package com.charan.bingediary.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.charan.bingediary.data.local.entity.UserMediaEntity
import com.charan.bingediary.data.local.entity.ReviewEntity
import com.charan.bingediary.data.local.dao.UserMediaDao
import com.charan.bingediary.data.local.dao.ReviewDao
import org.koin.core.annotation.Single

@Database(entities = [UserMediaEntity::class, ReviewEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
@Single
abstract class BingeDiaryDatabase : RoomDatabase() {
    abstract fun getUserMediaDao(): UserMediaDao
    abstract fun getReviewDao(): ReviewDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<BingeDiaryDatabase> {
    override fun initialize(): BingeDiaryDatabase
}