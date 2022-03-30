package com.lwj.lwjtest_coustomview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class LwjSinView: View {
    var mWidth: Int = 0
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet!!, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        initAttr(context, attributeSet)
        initPaint()
    }

    private fun initPaint() {

    }

    private fun initAttr(context: Context, attributeSet: AttributeSet) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawLine(canvas)
    }

    private fun drawLine(canvas: Canvas?) {

    }
}
