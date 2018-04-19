package com.taskail.mixion.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.taskail.mixion.data.models.local.Drafts
import com.taskail.mixion.data.models.local.RoomTags
import com.taskail.mixion.data.models.local.UserVotes
import com.taskail.mixion.utils.ArrayConverter

/**
 *Created by ed on 1/26/18.
 */
@Database(entities = [
    (RoomTags::class),
    (Drafts::class),
    (UserVotes::class)
], version = 3)
@TypeConverters(ArrayConverter::class)
abstract class MixionDatabase : RoomDatabase() {

    abstract fun tagsDao() : TagsDao
    abstract fun draftsDao() : DraftsDao
    abstract fun votesDao(): VotesDao

    companion object {
        private var INSTANCE: MixionDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context) : MixionDatabase{
            return synchronized(lock) {
                INSTANCE ?: Room.databaseBuilder(
                        context.applicationContext,
                        MixionDatabase::class.java,
                        "Mixion.db")
                        .fallbackToDestructiveMigration()
                        .build().apply {
                    INSTANCE = this
                }
            }
        }

    }
}