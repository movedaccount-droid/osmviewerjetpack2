package ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model

import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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

}

