package com.charan.bingediary.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.annotation.Single
import org.koin.core.context.GlobalContext
@Single
fun getDatabaseBuilder(context : Context): RoomDatabase.Builder<BingeDiaryDatabase> {
    val context = GlobalContext.get().get<Context>()
    val dbFile = context.getDatabasePath("binge_diary.db")
    return Room.databaseBuilder<BingeDiaryDatabase>(context, dbFile.absolutePath)
}
