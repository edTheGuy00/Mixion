package com.taskail.mixion.data.models.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 *Created by ed on 4/17/18.
 */

@Entity(tableName = "Votes")
data class UserVotes(
        var authorperm: String,
        var data: String,
        @PrimaryKey var uId: String = UUID.randomUUID().toString()) {

    fun containsMatch(search: String): Boolean {
        return authorperm == search
    }
}