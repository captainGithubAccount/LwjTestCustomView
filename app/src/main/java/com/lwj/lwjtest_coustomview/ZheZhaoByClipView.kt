package com.lwj.lwjtest_coustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.lwj.lwjtest_coustomview_testview.R

class ZheZhaoByClipView: View {

    val mBottomDrawlayerPaint: Paint? = null

    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet!!, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        initPaint()
    }

    private fun initPaint() {
        mBottomDrawlayerPaint?.let {


        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackLayer(canvas)
        drawTopLayer(canvas)

    }

    private fun drawTopLayer(canvas: Canvas?) {

        /*canvas?.saveLayer(0F,0F,width.toFloat(),height.toFloat(), mBottomDrawlayerPaint)
        canvas?.drawColor(resources.getColor(R.color.top_white_tran))
        canvas?.restore()*/
    }

    fun drawBackLayer(canvas: Canvas?) {
        canvas?.clipOutRect(20F,20F,120F,120F)
        //canvas?.clipRect(20F,20F,120F,120F)
        canvas?.drawColor(resources.getColor(R.color.back_tran))
        //canvas?.restore()
    }
}