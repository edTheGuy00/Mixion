package com.taskail.mixion.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 *Created by ed on 1/26/18.
 *
 * Data Access object for the Tags table
 */
@Dao interface TagsDao {

    @Query("SELECT * FROM Tags") fun getTags(): List<RoomTags>

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertTag(tags: RoomTags)

    /**
     * Delete all Tags.
     */
    @Query("DELETE FROM Tags") fun deleteTags()
}