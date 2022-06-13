package com.lwj.lwjtest_coustomview

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.example.lwj_common.common.ui.controll.tools.utils.JsonUtil
import java.lang.reflect.Type

fun<T> T.toFastJson(): String{
    return JSON.toJSONString(this)
}

inline fun<reified T> String.fromFastJsonInline(clz: T): T{
    return JSON.parseObject(this, T::class.java)
}

inline fun<reified T> String.fromFastJson(type: T): T {
    return JSON.parseObject(this, object : TypeReference<T>(){})
}

