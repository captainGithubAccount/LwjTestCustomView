package com.lwj.lwjtest_coustomview

import com.google.gson.*
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import java.io.IOException
import java.lang.reflect.Type


class MapDeserializerDoubleAsIntFix : JsonDeserializer<Map<String, Any?>> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Map<String, Any?> {
        return read(json) as Map<String, Any?>
    }

    fun read(ele: JsonElement): Any? {
        if(ele.isJsonArray) {
            val list: MutableList<Any?> = ArrayList()
            val arr = ele.asJsonArray
            for(anArr in arr) {
                list.add(read(anArr))
            }
            return list
        } else if(ele.isJsonObject) {
            val map: MutableMap<String, Any?> = LinkedTreeMap()
            val obj = ele.asJsonObject
            val entitySet = obj.entrySet()
            for((key, value) in entitySet) {
                map[key] = read(value)
            }
            return map
        } else if(ele.isJsonPrimitive) {
            val prim = ele.asJsonPrimitive
            if(prim.isBoolean) {
                return prim.asBoolean
            } else if(prim.isString) {
                return prim.asString
            } else if(prim.isNumber) {
                val num = prim.asNumber
                // here you can handle double int/long values
                // and return any type you want
                // this solution will transform 3.0 float to long values
                return if(Math.ceil(num.toDouble()) == num.toInt().toDouble()){
                    num.toInt()
                }else {
                    num.toDouble()
                }
            }
        }
        return null
    }
}

fun obtainGson(): Gson{
    val gsonBuilder = GsonBuilder()
    gsonBuilder.registerTypeAdapter(object :TypeToken<HashMap<String, Any?>>(){}.type,  MapDeserializerDoubleAsIntFix())
    return  gsonBuilder.create()
}

private val gsonAfterHandle: Gson by lazy { obtainGson() }

fun <T> T.toHandleJson(): String {
    return gsonAfterHandle.toJson(this)
}

fun <T> String.fromHandleJson(type: Type): T {

    return gson.fromJson(this, type)
}




//-------------------

private val gson by lazy { Gson() }

fun <T> String.fromJson(type: Type): T {

    return gson.fromJson(this, type)
}

fun <T> T.toJson(): String {
    return gson.toJson(this)
}
