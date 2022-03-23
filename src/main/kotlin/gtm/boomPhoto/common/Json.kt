package gtm.boomPhoto.common

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime

object Json {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeGsonAdapter())
        .create()

    private class LocalDateTimeGsonAdapter : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?)
                : LocalDateTime =
            LocalDateTime.parse(json?.asString)


        override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?)
                : JsonElement =
            JsonPrimitive(src.toString())

    }


    fun toJson(obj: Any?): String? {
        return try {
            ObjectMapper().writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    inline fun <reified T>fromJson(obj: String, token: JavaType): T {
        return try {
            ObjectMapper().readValue(obj, token)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}