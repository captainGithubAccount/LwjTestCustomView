package com.lwj.lwjtest_coustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import com.lwj.lwjtest_coustomview_testview.R

class ZheZhaoByLayerView: View {

    var mTopDrawlayerPaint: Paint? = null
    var mBottomDrawlayerPaint: Paint? = null

    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet!!, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        initPaint()
    }

    private fun initPaint() {
        mBottomDrawlayerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBottomDrawlayerPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        mBottomDrawlayerPaint?.color = resources.getColor(R.color.white)

        mTopDrawlayerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTopDrawlayerPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackLayer(canvas)
        drawTopLayer(canvas)

    }

    private fun drawTopLayer(canvas: Canvas?) {

//        canvas?.saveLayer(0F,0F,100F,100F, mTopDrawlayerPaint)
//        canvas?.drawColor(resources.getColor(R.color.back_tran))
//        canvas?.restore()
    }

    fun drawBackLayer(canvas: Canvas?) {
//        canvas?.saveLayer(0F,0F,width.toFloat(),height.toFloat(), mBottomDrawlayerPaint)
        canvas?.drawColor(resources.getColor(R.color.back_tran))
        mBottomDrawlayerPaint?.let { canvas?.drawRect(0F,0F,600F,600F, it) }
        //        canvas?.restore()
    }
}