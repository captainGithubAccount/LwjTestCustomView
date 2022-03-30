package com.lwj.lwjtest_coustomview

import com.example.lwj_common.common.ui.controll.tools.ktx.toJson
import com.example.oinkredito.base.ui.controll.activity.BaseDbActivity
import com.lwj.lwjtest_coustomview_testview.databinding.ActivityLineChartBinding
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class LineChartActivity : BaseDbActivity<ActivityLineChartBinding>(){
    val webSocketUrl: String = "wss://ws.binaryws.com/websockets/v3?app_id=1089"

    var data: ArrayList<String> = arrayListOf()
    override fun observe() {

    }

    private fun getConnetWebSocket(webSocketUrl: String): WebSocket{
        //val webSocketUrl: String = "wss://${hostName}:${port}"

        var webSocket = NetUtils.getOkHttpClient()
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
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                }

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                }
            })



    }

    override fun ActivityLineChartBinding.initView() {

        var webSocket = getConnetWebSocket(webSocketUrl)
        var jsonData = mutableMapOf<String, Any>()
        jsonData["ticks"] = "R_100"
        webSocket.send(jsonData.toJson())

        /*repeat(100){
            var ele = 11144.74 + Math.random() * (11355.08 - 11144.74)
            data?.add(String.format("%.2f", ele))//保留浮点数后两位

        }*/
        binding.lcvTest.setLineChartData(data)


        binding.tv60.setOnClickListener {
            binding.lcvTest.aXPointGapSize = 8
        }

        binding.tv30.setOnClickListener {
            binding.lcvTest.aXPointGapSize = 15
        }

        binding.tv15.setOnClickListener {
            binding.lcvTest.aXPointGapSize = 22
        }

        binding.tv3.setOnClickListener {
            binding.lcvTest.aXPointGapSize = 29
        }
    }


}