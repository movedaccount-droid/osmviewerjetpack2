package ac.uk.hope.osmviewerjetpack.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
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

@SuppressLint("SimpleDateFormat")
fun Calendar.toDateString(): String = SimpleDateFormat("yyyy-MM-dd").format(this.time)