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
@Dao interface DraftsDao {

    @Query("SELECT * FROM Drafts") fun getDrafts(): List<Drafts>



    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertDraft(draft: Drafts)


    /**
     * Delete all drafts.
     */
    @Query("DELETE FROM Drafts") fun deleteDraft()

    /**
     * Delete a draft by the ID.
     *
     * @param id The row ID.
     * @return A number of draft deleted. This should always be `1`.
     */
    @Query("DELETE FROM Drafts WHERE entryid = :entryid") fun deleteById(entryid: String): Int
}