package com.charan.bingediary.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.annotation.Single

@Single
fun getRoomDatabase(builder: RoomDatabase.Builder<BingeDiaryDatabase>): BingeDiaryDatabase {
    return builder
        .fallbackToDestructiveMigration(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .build()
}
