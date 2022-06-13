package com.lwj.lwjtest_coustomview

import android.util.Log
import java.util.*
import kotlin.collections.HashMap


/*

fun main(){
    var mAllStakesOffMap: HashMap<String, Any> = HashMap()

    for(i in 0..4){
        var uuid = UUID.randomUUID().toString()
        mAllStakesOffMap[uuid!!] = i
        var uuidY = "$uuid-y"
        mAllStakesOffMap[uuidY!!] = i

    }


    for((key,  value) in mAllStakesOffMap){
        mAllStakesOffMap.put(key, (value as Int)+100)

    }
    for((key,  value) in mAllStakesOffMap){

        println(key+ "\t"+  value)
    }


}*/

/*fun main() {
    var mAllStakesOffMap: HashMap<String, Any> = HashMap()

    for(i in 0..4) {
        var uuid = UUID.randomUUID().toString()
        mAllStakesOffMap[uuid!!] = i
        var uuidY = "$uuid-y"
        mAllStakesOffMap[uuidY!!] = i+100

        mAllStakesOffMap["removeKey"] = 9999999
    }
    mAllStakesOffMap.remove("removeKey")
    println(mAllStakesOffMap["removeKey"])
}*/

fun a(num: Number){

}

fun <T> T.isGsonIntToDouble(): Boolean {
    return if(this is Double) {
        Log.d("---", "Double")
        if(Math.ceil(this.toDouble()) == this.toFloat().toDouble()) {
            true
        } else {
            false
        }
    } else {
        false
    }
}

fun main(){
    val num: Number = 1.1
    if(Math.ceil(num.toDouble()) == num.toInt().toDouble()){
        println(num.toInt().toString() + "ttt")

    }else{
        println(num.toDouble().toString() + "bbb")
    }
   // println(Math.ceil(1.1))
    println()
}