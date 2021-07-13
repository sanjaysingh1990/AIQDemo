package com.wh.workmanager

import android.content.Context
import androidx.work.*
import com.wh.App
import java.util.concurrent.TimeUnit

class SocketReconnectionScheduler(context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
        if (!App.isInForeground()) return Result.failure()
        return Result.success()
    }

    companion object {
        fun cancel(context: Context) {
            WorkManager.getInstance(context)
                .cancelAllWorkByTag(SocketReconnectionScheduler::class.java.getSimpleName())
        }

        fun schedule(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build()
            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
                SocketReconnectionScheduler::class.java
            )
                .setConstraints(constraints).setInitialDelay(10, TimeUnit.SECONDS)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .addTag(SocketReconnectionScheduler::class.java.getSimpleName()).build()

            return oneTimeWorkRequest
        }
    }
}