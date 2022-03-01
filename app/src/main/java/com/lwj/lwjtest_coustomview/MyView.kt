package com.lwj.lwjtest_coustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.jvm.JvmOverloads

internal class MyTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**
     * 需要绘制的文字
     */
    private val mText = "Udf32fA"

    /**
     * 文本的颜色
     */
    private val mTextColor: Int

    /**
     * 文本的大小
     */
    private val mTextSize: Int

    /**
     * 绘制时控制文本绘制的范围
     */
    private val mBound: Rect
    private val mPaint: Paint

    //API21
    //    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    //        super(context, attrs, defStyleAttr, defStyleRes);
    //        init();
    //    }
    override fun onDraw(canvas: Canvas) {
        //绘制文字
        canvas.drawText(mText, (width / 2 - mBound.width() / 2).toFloat(), (height / 2 + mBound.height() / 2).toFloat(), mPaint)
    }

    init {
        //初始化
        mTextColor = Color.BLACK
        mTextSize = 100
        mPaint = Paint()
        mPaint.textSize = mTextSize.toFloat()
        mPaint.color = mTextColor
        //获得绘制文本的宽和高
        mBound = Rect()
        mPaint.getTextBounds(mText, 0, mText.length, mBound)
    }
}