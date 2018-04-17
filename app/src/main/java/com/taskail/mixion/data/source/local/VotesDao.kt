package com.taskail.mixion.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.taskail.mixion.data.models.local.UserVotes

/**
 *Created by ed on 4/17/18.
 *
 * Data Access object for the Votes table
 */
@Dao
interface VotesDao {

    @Query("SELECT * FROM Votes WHERE authorperm LIKE :authorperm") fun findVote(authorperm: String): List<UserVotes>

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertVotes(vote: UserVotes)

    /**
     * Delete all Votes.
     */
    @Query("DELETE FROM Votes") fun deleteVotes()
}