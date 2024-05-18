package ac.uk.hope.osmviewerjetpack.data.local.util

import java.util.Calendar

// can't use new date apis, too new
fun getCurrentCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal
}