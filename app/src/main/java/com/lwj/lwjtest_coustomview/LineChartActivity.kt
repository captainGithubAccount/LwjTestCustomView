package com.lwj.lwjtest_coustomview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.example.lwj_common.common.ui.controll.tools.ktx.fromJson
import com.example.lwj_common.common.ui.controll.tools.utils.JsonUtil
import com.example.oinkredito.base.ui.controll.activity.BaseDbActivity
import com.google.gson.JsonSyntaxException
import com.lwj.lwjtest_coustomview_testview.databinding.ActivityLineChartBinding
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class LineChartActivity : BaseDbActivity<ActivityLineChartBinding>() {
    var lineChartList: ArrayList<Double>? = null
    var timeTampList: ArrayList<Long>? = null
    val webSocketUrl: String = "wss://ws.binaryws.com/websockets/v3?app_id=1089"
    var mWebSocket: WebSocket? = null
    var mIsConnetion: Boolean = false
    var mTime: Int = 60

    var data: ArrayList<Double> = arrayListOf()
    override fun observe() {

    }



    private fun getConnetWebSocket(webSocketUrl: String) {

        NetUtils.getOkHttpClient()
            .newWebSocket(NetUtils.createRequest(webSocketUrl), object : WebSocketListener() {
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    t.printStackTrace()
                    super.onFailure(webSocket, t, response)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    try {
                        val data: WebSocketResponse = text.fromJson(WebSocketResponse::class.java)
                        showUi(data)
                    }catch(e: JsonSyntaxException){
                        Log.e("ddd", "Json格式转换异常")
                        e.printStackTrace()
                    }

                }


                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    mWebSocket = webSocket
                    mIsConnetion = true
                    queryAll()
                }
            })
    }


    //@RequiresApi(Build.VERSION_CODES.N)
    private fun showUi(data: WebSocketResponse) {
        if(data.msg_type == "history" && data.history != null && data.history.prices.isNotEmpty() && data.history.times.isNotEmpty() && data.echo_req != null){
            lineChartList = null
            timeTampList = null
            lineChartList = data.history.prices as ArrayList<Double>
            timeTampList = data.history.times as ArrayList<Long>
            Log.d("---",lineChartList?.maxOrNull().toString() + "----" + lineChartList?.minOrNull().toString())
            binding.lcvTest.setDatas(lineChartList!!, timeTampList!!)
            queryRealTimePoint()

        }else if(data.msg_type == "tick" && data.tick != null && !lineChartList.isNullOrEmpty() && !timeTampList.isNullOrEmpty()){
            lineChartList?.removeAt(0)
            lineChartList?.add(data.tick.quote)
            timeTampList?.removeAt(0)
            timeTampList?.add(data.tick.epoch)
            binding.lcvTest.setDatas(lineChartList!!, timeTampList!!, true)
        }
    }

    private fun showData() {

    }

    private fun queryAll() {
        var requestBody = mutableMapOf<String, Any>()
        requestBody["ticks_history"] =  "R_100"
        requestBody["count"] = 1800
        requestBody["end"] = "latest"
        requestBody["style"] = "ticks"
        mWebSocket?.send(requestBody.toJson());
    }

    private fun queryRealTimePoint(){
        var requestBody = mutableMapOf<String, Any>()
        requestBody["ticks"] =  "R_100"
        mWebSocket?.send(requestBody.toJson())
    }

    /*
    {"ticks_history": "R_100", "count": "31", "end": "latest", "style": "ticks"}
    {"ticks": "R_100"}
    */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("---stake序列化前", binding.lcvTest.getStakeDatas().toFastJson())
        outState.putString("Stake_state", binding.lcvTest.getStakeDatas().toFastJson())
    }

    override fun afterInitView(savedInstanceState: Bundle?) {
        super.afterInitView(savedInstanceState)
        if(savedInstanceState != null){
            val map: HashMap<String, Any?>? = (savedInstanceState.get("Stake_state") as String).fromFastJson(HashMap<String, Any?>())
            Log.d("---stake序列化后", map.toFastJson())
            binding.lcvTest.setStakeFromSaveInstance(map as HashMap<String, Any?>)
        }
    }

    override fun ActivityLineChartBinding.initView() {
        getConnetWebSocket(webSocketUrl)

        binding.llStake.setOnClickListener {
            binding.lcvTest.setStake(Color.GREEN)
        }



     /*   repeat(100){
            var ele = 11144.74 + Math.random() * (11355.08 - 11144.74)
            data?.add(String.format("%.2f", ele).toDouble())//保留浮点数后两位

        }*/
       // binding.lcvTest.setLineChartData(data)


        binding.tv60.setOnClickListener {
            binding.lcvTest.mTimeType = 60
        }

        binding.tv30.setOnClickListener {
            binding.lcvTest.mTimeType = 30
        }

        binding.tv15.setOnClickListener {
            binding.lcvTest.mTimeType = 15
        }

        binding.tv3.setOnClickListener {
            binding.lcvTest.mTimeType = 3
        }
    }


}