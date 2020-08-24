package com.example.market.util

import android.R
import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat

class AppUtil {
    companion object {
        fun booleanToInt(b: Boolean): Int {
            return if (b) {
                1
            } else {
                0
            }
        }

        public fun intToBoolean(b: Int): Boolean {
            return b == 1
        }
        fun sendNotification(
            context: Context, activity: Activity,
            title: String?, description: String?, data: String?
        ) {

            // Create an explicit content Intent that starts the main Activity.
            val intent = Intent(context, activity.javaClass)
            intent.putExtra(Constant.Extra.NOTIFICATION_DATA, data)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Construct a task stack.
            val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)

            // Add the main Activity to the task stack as the parent.
            stackBuilder.addParentStack(activity.javaClass)

            // Push the content Intent onto the stack.
            stackBuilder.addNextIntent(intent)

            // Get a PendingIntent containing the entire back stack.
            val notificationPendingIntent: PendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            // Get a notification builder that's compatible with platform versions >= 4
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)

            // Define the notification settings.
            // small style
            builder.setSmallIcon(R.drawable.sym_action_chat)
                .setTicker(title)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.sym_def_app_icon
                    )
                )
                .setColor(Color.GRAY)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(description)
                )
                .setContentIntent(notificationPendingIntent)

            // Get an instance of the Notification manager
            val mNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Issue the notification
            mNotificationManager.notify(0, builder.build())
        }


    }
}