package com.caique.thesubscribrerapp.data.entity.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.caique.thesubscribrerapp.data.entity.SubscriberEntity

@Dao
interface SubscriberDao {

    @Insert
    fun insert(subscriber: SubscriberEntity)

    @Update
    fun update(subscriber: SubscriberEntity)

    @Delete
    fun delete(subscriber: SubscriberEntity)


    @Query("SELECT * FROM subscribers ORDER BY uid ASC")
    suspend fun getAllSubscribers(): List<SubscriberEntity>

    @Query("SELECT * FROM subscribers WHERE uid = :subscriberId")
    suspend fun getSubscriberById(subscriberId: Int): SubscriberEntity?
}