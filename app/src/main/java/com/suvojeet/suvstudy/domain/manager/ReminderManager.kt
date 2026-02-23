package com.suvojeet.suvstudy.domain.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.suvojeet.suvstudy.receiver.StudyReminderReceiver
import java.util.Calendar

class ReminderManager(private val context: Context) {
    fun scheduleDailyReminder(hour: Int = 18, minute: Int = 0) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, StudyReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Next day if time already passed
        }

        try {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
