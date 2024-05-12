package com.caique.thesubscribrerapp

import android.app.Application
import com.caique.thesubscribrerapp.data.entity.database.SubscriberDatabase

class App: Application() {

    lateinit var database: SubscriberDatabase

    override fun onCreate() {
        super.onCreate()
        database = SubscriberDatabase.getInstance(this)
    }

}