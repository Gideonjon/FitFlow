package com.shoppitplus.fitlife.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String
)
data class PhoneCall(
    val name: String,
    val phone: String,
    val type: String,
    val time: String
)
