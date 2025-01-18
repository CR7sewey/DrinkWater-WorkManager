package com.example.drinkwater

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class NotificationWorker(val context: Context, val workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "your_channel_id" // channel 1
        val channelName = "Your Notifications Channel"

        // verficacao se user esta a rodar acima da versao do android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_water)
            .setContentTitle("Drink Water!!!")
            .setContentText("Fill your bottle.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build()
        val idNotification = Random.nextInt()

        notificationManager.notify(idNotification,notification)
        return Result.success()

    }
}