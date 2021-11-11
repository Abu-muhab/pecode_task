package com.abumuhab.pecodetask

import android.app.Application
import com.abumuhab.pecodetask.data.source.NotificationPagesRepository

class PecodeApplication : Application() {
    val pagesRepository = NotificationPagesRepository(this)
}