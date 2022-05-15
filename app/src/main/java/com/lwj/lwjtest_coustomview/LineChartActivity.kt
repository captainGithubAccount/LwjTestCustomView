package com.lwj.lwjtest_coustomview

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.lwj_common.common.ui.controll.tools.ktx.fromJson
import com.example.lwj_common.common.ui.controll.tools.ktx.isUseful
import com.example.lwj_common.common.ui.controll.tools.ktx.toJson
import com.example.lwj_common.common.ui.controll.tools.utils.DateUtil
import com.example.oinkredito.base.ui.controll.activity.BaseDbActivity
import com.google.gson.JsonSyntaxException
import com.lwj.lwjtest_coustomview_testview.databinding.ActivityLineChartBinding
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject
import java.util.stream.Collectors

class LineChartActivity : BaseDbActivity<ActivityLineChartBinding>() {
    val webSocketUrl: String = "wss://ws.binaryws.com/websockets/v3?app_id=1089"
    var mWebSocket: WebSocket? = null
    var mIsConnetion: Boolean = false
    var mTime: Int = 60

    var lineChartList: List<Double> = arrayListOf()

    var timeStampList: List<Long> = arrayListOf()
    var valueList: List<Double> = arrayListOf()

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
        if(data.history != null && data.msg_type == "history" && data.history.prices.isNotEmpty() && data.history.times.isNotEmpty() && data.echo_req != null){
            var lineChartList: List<Double> = data.history.prices
            var timeTampList: List<Long> = data.history.times
            binding.lcvTest.setDatas(lineChartList as ArrayList<Double>, timeTampList as ArrayList<Long>)
            binding.lcvTest.postInvalidate()

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

    /*
    {"ticks_history": "R_100", "count": "31", "end": "latest", "style": "ticks"}
    {"ticks": "R_100"}
    */

    override fun ActivityLineChartBinding.initView() {
        getConnetWebSocket(webSocketUrl)



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