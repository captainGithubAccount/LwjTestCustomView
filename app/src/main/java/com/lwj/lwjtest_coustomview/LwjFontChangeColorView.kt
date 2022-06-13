package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.lwj.lwjtest_coustomview_testview.R

class LwjFontChangeColorView : AppCompatTextView {
    var mStart: Int = 0
    var mAOriginColor: Int = Color.BLACK
    var mAChangeColorColor: Int = Color.RED

    var mPOriginPaint: Paint? = null
    var mPChangePaint: Paint? = null

    @Volatile
    var mCurrentClipProgress: Float = 0F
        get() = field
        set(value) {
            field = value
            this.invalidate()
        }

    var mSwipeDirection: Direction? = Direction.SWIPE_LEFT_TO_RIGHT

    enum class Direction {
        SWIPE_LEFT_TO_RIGHT, SWIPE_RIGHT_TO_LEFT
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes!!, 0)
    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int) : super(context, attributes, defStyleAttr) {
        initAttrs(context, attributes)
        initPaints()
    }

    private fun initAttrs(context: Context, attributes: AttributeSet) {
        val array: TypedArray = context.obtainStyledAttributes(attributes, R.styleable.LwjFontChangeColorView)
        mAOriginColor = array.getColor(R.styleable.LwjFontChangeColorView_originColor, Color.BLACK)
        mAChangeColorColor = array.getColor(R.styleable.LwjFontChangeColorView_changeColor, Color.RED)
        array.recycle()

    }


    private fun initPaints() {
        mPOriginPaint = Paint() //防抖动
        mPOriginPaint?.isDither = true
        mPOriginPaint?.isAntiAlias = true
        mPOriginPaint?.color = mAOriginColor
        mPOriginPaint?.textSize = textSize //from getTextSize()

        mPChangePaint = Paint()
        mPChangePaint?.isDither = true
        mPChangePaint?.isAntiAlias = true
        mPChangePaint?.color = mAChangeColorColor
        mPChangePaint?.textSize = textSize
    }

    /**
     * 实现思路
     * 利用clipRect的Api带来的裁减功能, 左边用一个画笔去画, 右边用另一个画笔去画, 不断的改变中间裁减位置值
     * */
    override fun onDraw(canvas: Canvas?) {/*
        * 当继承的不是view而是textView等其他控件 应该注释该行, 否则会多绘制一次文本, 并出现重叠现象
        */ //super.onDraw(canvas)

        val middle: Int = (mCurrentClipProgress * width.toFloat()).toInt()
        //Log.d("--LwjFontChangeColor", middle.toString())

        if(mSwipeDirection == Direction.SWIPE_LEFT_TO_RIGHT) {
            //绘制左边不变色区域
            drawClibText(canvas, mPChangePaint!!, 0, middle)

            //绘制右边 变色
            drawClibText(canvas, mPOriginPaint!!, middle, width)
        } else if(mSwipeDirection == Direction.SWIPE_RIGHT_TO_LEFT) {

            drawClibText(canvas, mPChangePaint!!, width - middle, width)

            drawClibText(canvas, mPOriginPaint!!, 0, width - middle)




        }

    }

    private fun drawClibText(canvas: Canvas?, paint: Paint, startClibPos: Int, endClibPos: Int) {
        // 将变色区域裁剪为左右两边(一边变色, 一边不变色), 注意是先裁减, 再画, 而不是先画, 后裁减
        canvas?.save()
        var rect = Rect(startClibPos, 0, endClibPos.toInt(), height)
        canvas?.clipRect(rect)

        //绘制文字的左边起始位置
        var text: String = text.toString()
        val bounds = Rect()
        mPOriginPaint?.getTextBounds(text, 0, text.length, bounds)
        val startDrawX = width / 2 - bounds.width() / 2

        //文字基线位置
        val fm: Paint.FontMetricsInt = mPOriginPaint?.fontMetricsInt!!
        val dy = (fm.bottom - fm.top) / 2 - fm.bottom
        val baseLine: Int = height / 2 + dy

        //绘制裁减后的文字
        canvas?.drawText(text, startDrawX.toFloat(), baseLine.toFloat(), paint!!)
        canvas?.restore()
    }


}