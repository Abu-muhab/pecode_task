package com.abumuhab.pecodetask.data.source

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abumuhab.pecodetask.data.NotificationPage

class NotificationPagesRepository(private val application: Application) {
    private var notificationPageDao: NotificationPageDao =
        AppDatabase.getInstance(application).notificationPageDao
//    private val _pages = MutableLiveData<List<NotificationPage>>()
//    private val pages: LiveData<List<NotificationPage>>
//        get() = _pages

    suspend fun addPage() {
        notificationPageDao.getPages().value?.let {
            val page = NotificationPage(0L, it.size + 1, "This is page ${it.size + 1}")
            notificationPageDao.insert(page)
//            val pages = arrayListOf(page)
//            pages.addAll(it)
//            pages.sortBy { it.number }
//            this._pages.value = pages
        }
    }

    suspend fun removePage() {
        notificationPageDao.deleteLast()
//        this._pages.value?.let {
//            /**
//             * If there is only one page left, do not perform remove operation
//             */
//            if (it.size == 1) {
//                return
//            }
//
//            val pages = it.toMutableList()
//            pages.removeLast()
//            this._pages.value = pages
    }
//    }

    fun observePages(): LiveData<List<NotificationPage>> {
        return notificationPageDao.getPages()
    }
}