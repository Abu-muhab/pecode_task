package com.abumuhab.pecodetask.data.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.abumuhab.pecodetask.data.NotificationPage

@Dao
interface NotificationPageDao {
    @Insert
    suspend fun insert(notificationPage: NotificationPage)

    @Query("DELETE FROM notification_pages_table WHERE number IN (SELECT number FROM notification_pages_table ORDER BY number DESC LIMIT 1)")
    suspend fun deleteLast()

    @Query("SELECT * FROM notification_pages_table")
    fun observePages(): LiveData<List<NotificationPage>>

    @Query("SELECT * FROM notification_pages_table")
    suspend fun getPages(): List<NotificationPage>
}