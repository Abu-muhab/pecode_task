package com.abumuhab.pecodetask.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_pages_table")
data class NotificationPage(
    @PrimaryKey(autoGenerate = true) var dbId: Long = 0L,
    val number: Int,
    val description: String
)