package com.example.drinkwater

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {
        for (i in 0..100000) {
            println("Executing $i")
        }
        return Result.success()
    }
}