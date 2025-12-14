package com.shoppitplus.fitlife.routines

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message") ?: "Workout Reminder"
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}