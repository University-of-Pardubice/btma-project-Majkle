package bmta.sem

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notification_id", 0)
        val notificationTitle = intent.getStringExtra("notification_title")
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager

        val notification = NotificationCompat.Builder(context, "channel_id")
            .setContentTitle(notificationTitle)
            .setContentText("Nastal čas na: $notificationTitle")
            .setSmallIcon(R.drawable.baseline_schedule_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
