package com.bhaveshsp.to_do.utility

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import com.bhaveshsp.to_do.R
import java.util.*

class TodoNotificationService : IntentService("TodoNotificationService") {
    override fun onHandleIntent(intent: Intent?) {
        val title = intent!!.getStringExtra(NOTIFICATION_TITLE_EXTRA)
        val description = intent.getStringExtra(NOTIFICATION_DESCRIPTION_EXTRA)
        val uuid = intent.getSerializableExtra(NOTIFICATION_UUID_EXTRA) as UUID
        val deleteIntent = Intent(this,DeleteTodoService::class.java)
        deleteIntent.putExtra(NOTIFICATION_UUID_EXTRA,uuid)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification.Builder(this)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_done)
            .setPriority(Notification.PRIORITY_HIGH)
            .setDeleteIntent(PendingIntent.getService(this,uuid.hashCode(),deleteIntent,PendingIntent.FLAG_UPDATE_CURRENT))
            .setDefaults(Notification.DEFAULT_ALL)
            .build()
        notificationManager.notify(NOTIFICATION_ID,notification)
    }

}
