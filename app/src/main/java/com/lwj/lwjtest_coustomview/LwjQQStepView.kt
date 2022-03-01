package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.lwj.lwjtest_coustomview_testview.R

class LwjQQStepView: View {
    var outColor: Int = 0
    var innerColor: Int = 0
    var textStepColor: Int = 0
    var textStepSize: Int = 0
    var arcWidth: Int = 0

    var mOutPaint: Paint? = null
    var mOutRectPaint: Paint? = null


    //Rectf等需要绘制的对象不要放到方法中绘制


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet!!, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        initAttrs(context, attributeSet)
        initPaint()
    }

    private fun initAttrs(context: Context, attributeSet: AttributeSet) {
        //模板代码
        val array: TypedArray = context.obtainStyledAttributes(attributeSet , R.styleable.LwjQQStepView)

        outColor = array.getColor(R.styleable.LwjQQStepView_arcOuterColor, ContextCompat.getColor(context, R.color.color_1296DB))
        innerColor = array.getColor(R.styleable.LwjQQStepView_arcInnerColor, ContextCompat.getColor(context, R.color.color_969696))
        textStepColor = array.getColor(R.styleable.LwjQQStepView_textStepColor, ContextCompat.getColor(context, R.color.color_00DE00))
        textStepSize = array.getDimensionPixelSize(R.styleable.LwjQQStepView_textStepSize, sp2dp(16))
        arcWidth = array.getDimensionPixelSize(R.styleable.LwjQQStepView_arcWidth, 6)

        //模板代码
        array.recycle()
    }

    private fun initPaint() {
        mOutPaint = Paint()
        mOutPaint?.isAntiAlias = true
        mOutPaint?.strokeWidth = arcWidth.toFloat()
        mOutPaint?.color =  outColor
        mOutPaint?.strokeCap = Paint.Cap.ROUND//将圆弧的两个出口用半圆来封闭
        mOutPaint?.style = Paint.Style.STROKE


        mOutRectPaint = Paint()
        mOutRectPaint?.isAntiAlias = true
        mOutRectPaint?.strokeWidth = 2F
        mOutRectPaint?.color = Color.RED
        mOutRectPaint?.style = Paint.Style.STROKE





    }

    private fun sp2dp(spValue: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue.toFloat(), resources.displayMetrics).toInt()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //注意: 宽高需要保持一致
        val widthSpecMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode: Int = MeasureSpec.getMode(heightMeasureSpec)

        var mWidth = MeasureSpec.getSize(widthMeasureSpec)
        var mHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(if(mWidth > mHeight) mWidth else mHeight, if(mWidth > mHeight) mWidth else mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //val rectF: RectF = RectF(0F, 0F,width.toFloat(),  height.toFloat())//分别为距型左上右下四个坐标值
        //画外圆弧顶部左右都有一部分曲线显示为直线, 即边缘没显示完整  现象原因: 描边有宽度 arcWidth （注意getWidth()获取的值是包含描边宽度在内的）, 解决如下

        var rectF = RectF(arcWidth/2.toFloat(), arcWidth/2.toFloat(), (width - arcWidth/2).toFloat(), (height - arcWidth/2).toFloat())
        canvas?.drawArc(rectF, 135F, 270F, false, mOutPaint!!)
        canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mOutRectPaint!!)
    }


}