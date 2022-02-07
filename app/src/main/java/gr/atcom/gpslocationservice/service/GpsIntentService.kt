package gr.atcom.gpslocationservice.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import gr.atcom.gpslocationservice.MainActivity
import gr.atcom.gpslocationservice.R
import gr.atcom.gpslocationservice.application.GpsLocationApplication
import gr.atcom.gpslocationservice.geofence.GeofenceUtil
import timber.log.Timber


class GpsIntentService : IntentService("GpsIntentService") {

    override fun onHandleIntent(intent: Intent?) {
    }

    private fun checkIfServiceIsRunning() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                Timber.d("Service is running")
                mainHandler.postDelayed(this, 3000)
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GeofenceUtil(this) // start geofencing

        val notificationPendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { it ->
                PendingIntent.getActivity(this, 0, it, 0)
            }

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(getString(R.string.default_notification_channel_id), "Gps Location Foreground Channel")
        } else {
            getString(R.string.default_notification_channel_id)
        }

        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("GPS Tracker")
            .setContentText("Live location updates...")
            .setSmallIcon(R.drawable.ic_location_pin)
            .setContentIntent(notificationPendingIntent)
            .build()

        startForeground(15, notification)
        return Service.START_STICKY
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("Destroyed")
    }


    companion object {
        var isServiceRunning: Boolean = false

        @JvmStatic
        fun startService(context: Context) {
            val intent = Intent(context, GpsIntentService::class.java)
            context.startForegroundService(intent)
            isServiceRunning = true
        }

        @JvmStatic
        fun stopService(context: Context) {
            val intent = Intent(context, GpsIntentService::class.java)
            context.stopService(intent)
            isServiceRunning = false
        }

        @JvmStatic
        fun onFabClicked(context: Context): Boolean {
            if (isServiceRunning) {
                stopService(context)
            } else {
                startService(context)
            }
            return isServiceRunning
        }
    }
}