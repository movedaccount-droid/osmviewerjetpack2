package ac.uk.hope.osmviewerjetpack.data.local.util

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TrackLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TypeLocal
import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class Converters {

    // room can't store lists and gson can't serialize the abstract uri class,
    // so we convert to string list format and serialize as json. ow!
    // https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity

    @TypeConverter
    fun uriListToJson(value: List<Uri>): String {
        return Gson().toJson(value.map(Uri::toString))
    }

    @TypeConverter
    fun jsonToUriList(value: String): List<Uri> {
        val type = object : TypeToken<List<String>>(){}.type
        return Gson()
            .fromJson<List<String>>(value, type)
            .map(Uri::decode)
            .map(Uri::parse)
    }

    // tags map
    @TypeConverter
    fun nullableStringIntMapToJson(value: Map<String, Int>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToNullableStringIntMap(value: String): Map<String, Int>? {
        val type = object : TypeToken<Map<String, Int>?>(){}.type
        return Gson().fromJson(value, type)
    }

    // types list
    @TypeConverter
    fun typesListToJson(value: List<TypeLocal>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToTypesList(value: String): List<TypeLocal> {
        val type = object : TypeToken<List<TypeLocal>>(){}.type
        return Gson().fromJson(value, type)
    }

    // tracks list
    @TypeConverter
    fun tracksListToJson(value: List<TrackLocal>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToTracksList(value: String): List<TrackLocal> {
        val type = object : TypeToken<List<TrackLocal>>(){}.type
        return Gson().fromJson(value, type)
    }

    // calendar
    @TypeConverter
    fun calendarToLong(value: Calendar): Long {
        return value.timeInMillis
    }

    @TypeConverter
    fun longToCalendar(value: Long): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = value
        return cal
    }

    // newReleases, list of mbids
    @TypeConverter
    fun stringListToJson(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(value, type)
    }

}

