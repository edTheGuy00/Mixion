package com.taskail.mixion.data.models.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 *Created by ed on 3/5/18.
 */

@Entity(tableName = "UserProfile")
data class UserProfile @JvmOverloads constructor(
        @ColumnInfo(name = "name") var id: String = "",
        @ColumnInfo(name = "profile_image") var profileImage: String = "",
        @ColumnInfo(name = "about") var about: String = "",
        @ColumnInfo(name = "website") var website: String,
        @ColumnInfo(name = "post_count") var postCount: Int,
        @ColumnInfo(name = "sbd_balance") var sbdBalance: String,
        @ColumnInfo(name = "reputation") var reputation: String,
        @ColumnInfo(name = "follower_count") var followersCount: Int,
        @ColumnInfo(name = "following_count") var followingCount: Int

)