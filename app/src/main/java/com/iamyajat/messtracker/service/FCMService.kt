package com.iamyajat.messtracker.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.util.NotificationHelper

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        msg.notification.let {
            if (it != null) {
                NotificationHelper.sendNotification(
                    this,
                    it.title!!,
                    it.body!!,
                    it.body!!,
                    getString(R.string.default_notification_channel_name),
                    Integer.MAX_VALUE
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d("TOKEN FCM", token)
    }
}