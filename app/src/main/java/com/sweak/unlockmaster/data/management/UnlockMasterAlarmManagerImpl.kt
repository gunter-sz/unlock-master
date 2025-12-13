package com.sweak.unlockmaster.data.management

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.sweak.unlockmaster.domain.management.UnlockMasterAlarmManager
import com.sweak.unlockmaster.domain.model.DailyWrapUpNotificationsTime
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject
import javax.inject.Named

class UnlockMasterAlarmManagerImpl @Inject constructor(
    private val alarmManager: AlarmManager,
    private val timeRepository: TimeRepository,
    @Named("DailyWrapUpAlarmIntent") private val dailyWrapUpAlarmIntent: Intent,
    private val application: Application
) : UnlockMasterAlarmManager {

    override fun scheduleNewDailyWrapUpNotifications(
        dailyWrapUpNotificationsTime: DailyWrapUpNotificationsTime
    ) {
        val alarmPendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            DAILY_WRAP_UP_ALARM_REQUEST_CODE,
            dailyWrapUpAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            timeRepository.getFutureTimeInMillisOfSpecifiedHourOfDayAndMinute(
                dailyWrapUpNotificationsTime.hourOfDay,
                dailyWrapUpNotificationsTime.minute
            ),
            AlarmManager.INTERVAL_DAY,
            alarmPendingIntent
        )
    }

    companion object {
        const val DAILY_WRAP_UP_ALARM_REQUEST_CODE = 100
    }
}