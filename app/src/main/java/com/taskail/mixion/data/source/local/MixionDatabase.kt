package com.taskail.mixion.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 *Created by ed on 1/26/18.
 */
@Database(entities = [(RoomTags::class)], version = 1)
abstract class MixionDatabase : RoomDatabase() {

    abstract fun tagsDao() : TagsDao

    companion object {
        private var INSTANCE: MixionDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context) : MixionDatabase{
            return synchronized(lock) {
                INSTANCE ?: Room.databaseBuilder(
                        context.applicationContext,
                        MixionDatabase::class.java,
                        "Mixion.db")
                        .build().apply {
                    INSTANCE = this
                }
            }
        }

    }
}