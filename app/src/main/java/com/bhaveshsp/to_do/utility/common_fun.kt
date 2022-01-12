package com.bhaveshsp.to_do.utility

import android.content.Context
import android.util.Log
import com.bhaveshsp.to_do.R
import java.text.SimpleDateFormat
import java.util.*

fun checkToday(today: Calendar, scheduledCalendar: Calendar):Boolean{
    if (today.get(Calendar.MONTH) == scheduledCalendar.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) == scheduledCalendar.get(
            Calendar.DAY_OF_MONTH)){
        return true
    }
    return false
}
fun checkTomorrow(scheduledCalendar: Calendar):Boolean{
    val tomorrow = Calendar.getInstance()
    tomorrow.add(Calendar.DAY_OF_MONTH,1)
    if (tomorrow.get(Calendar.MONTH) == scheduledCalendar.get(Calendar.MONTH) && tomorrow.get(
            Calendar.DAY_OF_MONTH) == scheduledCalendar.get(Calendar.DAY_OF_MONTH)){
        return true
    }
    return false
}
fun getDate(scheduledCalendar: Calendar,context: Context):String{
    val dayOfMonth = scheduledCalendar.get(Calendar.DAY_OF_MONTH)
    val month = scheduledCalendar.getDisplayName(Calendar.MONTH,Calendar.SHORT, Locale.US)
    Log.d(TAG, "getDate: Date Set ${context.getString(R.string.date_format,dayOfMonth,month)}")
    return context.getString(R.string.date_format,dayOfMonth,month)
}
fun getTime(scheduledCalendar: Calendar):String{
    val simpleDateFormat = SimpleDateFormat("h:mm a", Locale.US)
    Log.d(TAG, "getTime: Time Set ${simpleDateFormat.format(scheduledCalendar.time)}")
    return simpleDateFormat.format(scheduledCalendar.time)
}