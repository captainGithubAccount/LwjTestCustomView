package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
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
    var mInnerPaint: Paint? = null
    var mTextPaint: Paint? = null
    var mOutRectPaint: Paint? = null


    var mMaxStep: Int = 0
    var mCurrentStep: Int = 0


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

        innerColor = array.getColor(R.styleable.LwjQQStepView_arcOuterColor, ContextCompat.getColor(context, R.color.color_1296DB))
        outColor = array.getColor(R.styleable.LwjQQStepView_arcInnerColor, ContextCompat.getColor(context, R.color.color_969696))
        textStepColor = array.getColor(R.styleable.LwjQQStepView_textStepColor, ContextCompat.getColor(context, R.color.color_00DE00))
        textStepSize = array.getDimensionPixelSize(R.styleable.LwjQQStepView_textStepSize, sp2dp(16))
        arcWidth = array.getDimensionPixelSize(R.styleable.LwjQQStepView_arcWidth, 6)

        //模板代码
        array.recycle()
    }

    private fun initPaint() {
        //外圆弧
        mOutPaint = Paint()
        mOutPaint?.isAntiAlias = true
        mOutPaint?.strokeWidth = arcWidth.toFloat()
        mOutPaint?.color =  outColor
        mOutPaint?.strokeCap = Paint.Cap.ROUND//将圆弧的两个出口用半圆来封闭
        mOutPaint?.style = Paint.Style.STROKE//画笔空心

        //内圆弧 注意不能写死 按照百分比显示  是使用者设置的从外面传
        mInnerPaint = Paint()
        mInnerPaint?.isAntiAlias = true
        mInnerPaint?.strokeWidth = arcWidth.toFloat()
        mInnerPaint?.color =  innerColor
        mInnerPaint?.strokeCap = Paint.Cap.ROUND//将圆弧的两个出口用半圆来封闭
        mInnerPaint?.style = Paint.Style.STROKE//画笔空心


        //最外层正方形
        mOutRectPaint = Paint()
        mOutRectPaint?.isAntiAlias = true
        mOutRectPaint?.strokeWidth = 2F
        mOutRectPaint?.color = Color.RED
        mOutRectPaint?.style = Paint.Style.STROKE

        //文字画笔
        mTextPaint = Paint()
        mTextPaint?.isAntiAlias = true
        mTextPaint?.color = textStepColor
        mTextPaint?.textSize = textStepSize.toFloat()





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
        //画外弧
        canvas?.drawArc(rectF, 135F, 270F, false, mOutPaint!!)

        //画内弧
        val sweepAngle: Float = mCurrentStep / mMaxStep.toFloat()//扫过的角度, 注意int/int为int类型，除完后强转无效, 故分子分母必须有一个为Float类型
        canvas?.drawArc(rectF, 135F, sweepAngle * 270, false, mInnerPaint!!)

        //绘制的文字内容
        val text = mCurrentStep.toString()
        //param: 绘制文字的左边起始位置(即圆弧整个宽的一半也就是最中间位置减去中间文本的一半长度就是左边起始位置)
        val textBounds = Rect()
        mTextPaint?.getTextBounds(text, 0, text.length, textBounds)
        val startDrawX: Int = width/2 - textBounds.width()/2
        //param: 文字的基线
        val fm: Paint.FontMetricsInt = mTextPaint!!.fontMetricsInt
        val baseLine = height/2 + (fm.bottom - fm.top)/2 - fm.bottom
//或:        val baseLine = height/2 -(fm.bottom - fm.top)/2 - fm.top
        //画文字
        canvas?.drawText(text, startDrawX.toFloat(), baseLine.toFloat(), mTextPaint!!)

        //画最外层距形
        canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mOutRectPaint!!)




    }



    @Synchronized
     fun setCurrentStep(currentStep: Int) {
        this.mCurrentStep = currentStep

        //不断绘制
        invalidate()
    }

    @Synchronized
     fun setMaxStep(maxStep: Int) {
        this.mMaxStep = maxStep
    }


}