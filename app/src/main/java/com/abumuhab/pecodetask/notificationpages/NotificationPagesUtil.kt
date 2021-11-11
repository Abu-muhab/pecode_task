package com.abumuhab.pecodetask.notificationpages

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "notification_pages_channel"
        val descriptionText = "Shows notifications from a notification fragment page"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("npc", name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}