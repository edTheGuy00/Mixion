package com.taskail.mixion.data.models.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 *Created by ed on 2/28/18.
 */

@Entity(tableName = "MyBlog")
data class UserBlog @JvmOverloads constructor(
        @ColumnInfo(name = "id") var id: Int = 0,
        @ColumnInfo(name = "author") var author: String = "",
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "body") var body: String = ""
)