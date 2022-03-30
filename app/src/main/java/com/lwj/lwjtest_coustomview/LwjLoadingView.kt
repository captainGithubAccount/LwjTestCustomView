package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.lwj.lwjtest_coustomview_testview.R

class LwjLoadingView: View {
    var mCurrentDrawing: CurrentDrawing = CurrentDrawing.CIRCLE

    var mPRectPaint: Paint? = null
    var mPCirclePaint: Paint? = null
    var mPTrianglePaint: Paint? = null

    var mARectColor: Int = Color.RED
    var mACircleColor: Int = Color.YELLOW
    var mATriangleColor: Int = Color.GREEN

    var mPath: Path? = null


    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet!!, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStryleAttr: Int): super(context, attributeSet, defStryleAttr){
        initAttrs(context, attributeSet)
        initPaints()
    }

    private fun initAttrs(context: Context, attributeSet: AttributeSet) {
        val array: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LwjLoadingView)

        mARectColor = array.getColor(R.styleable.LwjLoadingView_rectColor, Color.RED)
        mACircleColor = array.getColor(R.styleable.LwjLoadingView_circleColor, Color.YELLOW)
        mATriangleColor = array.getColor(R.styleable.LwjLoadingView_triangleColor, Color.GREEN)
        array.recycle()
    }

    private fun initPaints() {



        mPRectPaint = Paint()
        mPRectPaint?.isAntiAlias = true
        mPRectPaint?.isDither = true
        mPRectPaint?.style = Paint.Style.FILL
        mPRectPaint?.color = mARectColor

        mPCirclePaint = Paint()
        mPCirclePaint?.isAntiAlias = true
        mPCirclePaint?.isDither = true
        mPCirclePaint?.style = Paint.Style.FILL
        mPCirclePaint?.color = mACircleColor

        mPTrianglePaint= Paint()
        mPTrianglePaint?.isAntiAlias = true
        mPTrianglePaint?.isDither = true
        mPTrianglePaint?.style = Paint.Style.FILL
        mPTrianglePaint?.color = mATriangleColor

    }

    enum class CurrentDrawing{
        CIRCLE, TRIANGLE, RECT
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        when(mCurrentDrawing){
            CurrentDrawing.CIRCLE -> {
                canvas?.drawCircle((width/2).toFloat(), (width/2).toFloat(), (width/2).toFloat(), mPCirclePaint!!)
            }

            CurrentDrawing.TRIANGLE -> {
                if(mPath == null){
                    mPath = Path()
                    mPath!!.moveTo((width / 2).toFloat(), 0F)
                    mPath!!.lineTo(0F, (width/2*Math.sqrt(3.0)).toFloat())
                    mPath!!.lineTo(width.toFloat(), (width/2*Math.sqrt(3.0)).toFloat())
                    mPath!!.close()

                }
                canvas?.drawPath(mPath!!, mPTrianglePaint!!)

            }

            CurrentDrawing.RECT -> {
                canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mPRectPaint!!)
            }
        }
    }

    fun changeCurrentDrawing(){
        when(mCurrentDrawing){
            CurrentDrawing.CIRCLE -> {
                mCurrentDrawing = CurrentDrawing.TRIANGLE
            }
            CurrentDrawing.TRIANGLE -> {
                mCurrentDrawing = CurrentDrawing.RECT
            }
            CurrentDrawing.RECT -> {
                mCurrentDrawing = CurrentDrawing.CIRCLE
            }
        }
        invalidate()
    }



}