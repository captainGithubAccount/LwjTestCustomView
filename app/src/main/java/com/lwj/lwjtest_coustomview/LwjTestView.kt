package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue

import android.view.View
import com.lwj.lwjtest_coustomview_testview.R


class LwjTestView : View {
    private var mText: String? = null
    private var mTextSize: Int = 15 //默认15
    private var mColor: Int = Color.BLACK

    private var mPaint: Paint? = null

    constructor(mContext: Context) : this(mContext,null)

    constructor(mContext: Context, attrs: AttributeSet?) : this(mContext, attrs!!,0)

    constructor(mContext: Context, attrs: AttributeSet,defStyleAttr:Int) : super(mContext, attrs,defStyleAttr) {
        init(mContext, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        //模板代码
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LwjTestView)

        mText = array.getString(R.styleable.LwjTestView_myText)

        mTextSize = array.getDimensionPixelSize(R.styleable.LwjTestView_myTextSize, sp2px(15))

        mColor = array.getColor(R.styleable.LwjTestView_textColor, Color.BLACK)

        //模板代码 -- 回收
        array.recycle()

        //初始化画笔
        mPaint = Paint()
        mPaint?.run{
            //设置抗锯齿
            isAntiAlias = true
            //设置字体大小和颜色
            textSize = mTextSize.toFloat()
            color = mColor
        }

    }

    private fun sp2px(spValue: Int): Int {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue.toFloat(), resources.displayMetrics).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //模板代码
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //1、确定的值，这个时候不需要计算，给的多少就是多少
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        //2、若android: width为wrap_content, 需要计算
        if(widthMode == MeasureSpec.AT_MOST){

            val bounds = Rect()
            mPaint?.getTextBounds(mText, 0, mText?.length!!, bounds)//获取文本rect
            width = bounds.width() + paddingLeft + paddingEnd
        }

        if(heightMode == MeasureSpec.AT_MOST){
            val bounds = Rect()
            mPaint?.getTextBounds(mText, 0, mText?.length!!, bounds)
            height = bounds.height() + paddingTop + paddingBottom
        }


        //模板代码
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //画文本
        val fontMetrics: Paint.FontMetricsInt = mPaint!!.fontMetricsInt
        val dy: Int = -(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.top
        val baseLine = height/2 + dy
        val x: Int = paddingLeft


        if(mText != null && mPaint != null){
            canvas?.drawText(mText!!, x.toFloat(), baseLine.toFloat(), mPaint!!)
        }

    }

}

