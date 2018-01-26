package com.taskail.mixion.data.source.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 *Created by ed on 1/26/18.
 */

@Entity(tableName = "tags")
data class RoomTags @JvmOverloads constructor(

        @ColumnInfo(name = "tag") var tag: String = "",
        @ColumnInfo(name = "posts") var posts: Int = 0,
        @ColumnInfo(name = "comments") var comments: Int = 0
) {

    val isEmpty get() =tag.isEmpty()
}