package com.caique.thesubscribrerapp.data.entity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.caique.thesubscribrerapp.data.entity.SubscriberEntity
import com.caique.thesubscribrerapp.data.entity.dao.SubscriberDao

@Database(entities = [SubscriberEntity::class], version = 1)
abstract class SubscriberDatabase : RoomDatabase() {

    abstract fun subscriberDao(): SubscriberDao

    companion object {
        @Volatile
        private var INSTANCE: SubscriberDatabase? = null

        fun getInstance(context: Context): SubscriberDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SubscriberDatabase::class.java,
                    "subscriber_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
