package com.taskail.mixion.data.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 *Created by ed on 1/26/18.
 */

@Entity(tableName = "Tags")
data class RoomTags @JvmOverloads constructor(

        @ColumnInfo(name = "tag") var tag: String = "",
        @ColumnInfo(name = "posts") var posts: Int = 0,
        @ColumnInfo(name = "comments") var comments: Int = 0,
        @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {

    val isEmpty get() =tag.isEmpty()
}