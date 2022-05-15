package com.lwj.lwjtest_coustomview

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lwj_common.common.ui.controll.tools.utils.DateUtil
import java.util.stream.Collectors

class TestList {

}


@RequiresApi(Build.VERSION_CODES.N)
fun main(){
    val list = arrayListOf("1", "9", "4")
    list.removeAt(0)
    println(list.get(0).toString())

    val list2 = listOf("5")
    val list3 = list2 + list

    println(list3.joinToString())
    val list4 = listOf<Long>(1648871018,
        1648871020,
        1648871022,
        1648871024,
        1648871026,
        1648871028,
        1648871030,
        1648871032,
        )

    val timeData: List<String> = list4.stream().map{ DateUtil.stampToDate(it) }.collect(Collectors.toList())
    println(timeData.toString())

    if(true){
        test(OnClickListener { print(it) })
    }
}

fun test(listener: OnClickListener){
    val marsProperty = "kotlin函数式调用"
    listener.onClick(marsProperty)
}

//适用于只调用一次, 若调用多次还是传this，传统写法吧
class OnClickListener(inline val clickListener: (marsProperty:String) -> Unit) {
    fun onClick(marsProperty:String) = clickListener(marsProperty)
}