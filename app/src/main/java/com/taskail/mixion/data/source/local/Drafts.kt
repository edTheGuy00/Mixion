package com.taskail.mixion.data.source.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 *Created by ed on 1/26/18.
 */

@Entity(tableName = "Drafts")
data class Drafts @JvmOverloads constructor(

        @ColumnInfo(name = "tag") var title: String = "",
        @ColumnInfo(name = "summary") var summary: String = "",
        @ColumnInfo(name = "tags") var tags:  List<String> = emptyList(),
        @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {

    val isEmpty get() = title.isEmpty()

}