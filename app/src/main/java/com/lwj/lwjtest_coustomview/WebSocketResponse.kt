package com.lwj.lwjtest_coustomview

import java.util.concurrent.Flow

data class WebSocketResponse(
    val echo_req: EchoReq?,
    val history: History?,
    val msg_type: String?,
    val subscription: Subscription?,
    val tick: Tick?,
    val pip_size: String?
)

data class Subscription(
    val id: String
)

data class EchoReq(
    val count: String,
    val end: String,
    val style: String,
    val ticks_history: String,
    val ticks: String
)

data class History(
    val prices: List<Double>,
    val times: List<Long>
)

data class Tick(
    val ask: Double,
    val bid: Double,
    val epoch: Int,
    val id: String,
    val pip_size: Int,
    val quote: Double,
    val symbol: String
)
