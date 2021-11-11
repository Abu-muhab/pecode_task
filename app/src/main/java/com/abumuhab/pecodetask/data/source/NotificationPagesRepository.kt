package com.abumuhab.pecodetask.data.source

import android.content.Context
import androidx.lifecycle.LiveData
import com.abumuhab.pecodetask.data.NotificationPage

class NotificationPagesRepository {
    private lateinit var notificationPageDao: NotificationPageDao

    companion object {
        private var INSTANCE: NotificationPagesRepository? = null
        fun getInstance(context: Context): NotificationPagesRepository {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = NotificationPagesRepository()
                    instance.notificationPageDao =
                        AppDatabase.getInstance(context).notificationPageDao
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    suspend fun addPage() {
        val size = notificationPageDao.getPages().size
        val page = NotificationPage(0L, size + 1, "This is page ${size + 1}")
        notificationPageDao.insert(page)
    }

    suspend fun removePage() {
        notificationPageDao.deleteLast()
    }

    fun observePages(): LiveData<List<NotificationPage>> {
        return notificationPageDao.observePages()
    }
}