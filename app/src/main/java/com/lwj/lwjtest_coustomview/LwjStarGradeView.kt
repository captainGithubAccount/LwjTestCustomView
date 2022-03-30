package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withTranslation
import com.lwj.lwjtest_coustomview_testview.R
import java.lang.RuntimeException

class LwjStarGradeView : View {

    var mAStarNormalBitmap: Bitmap? = null
    var mAStarSelectBitmap: Bitmap? = null
    var mAStarNumbers: Int = 0
    var mAStarMargin: Int = 4

    var mPStarNumberPaint: Paint? = null
    var mCurrentSelectStarNumber = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initAttrs(context, attributeSet)
        initPaints()

    }

    private fun initPaints() {

    }

    private fun sp2px(spValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue.toFloat(), resources.displayMetrics)
            .toInt()
    }

    private fun initAttrs(context: Context, attributeSet: AttributeSet?) {
        val array: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LwjStarGradeView)
        val startNormalId: Int = array.getResourceId(R.styleable.LwjStarGradeView_starNormal, R.drawable.ic_star)
        if(startNormalId == 0) {
            throw  RuntimeException("请设置资源属性starNormal")
        }
        mAStarNormalBitmap = BitmapFactory.decodeResource(resources, startNormalId)

        val startSelectId: Int = array.getResourceId(R.styleable.LwjStarGradeView_starSelect, R.drawable.ic_star_select)
        if(startNormalId == 0) {
            throw  RuntimeException("请设置资源属性starFocus")
        }
        mAStarSelectBitmap = BitmapFactory.decodeResource(resources, startSelectId)

        mAStarNumbers = array.getInt(R.styleable.LwjStarGradeView_starNumber, 0)

        mAStarMargin = array.getDimensionPixelSize(R.styleable.LwjStarGradeView_starMargin, sp2px(4))

        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height: Int = mAStarNormalBitmap?.height!!
        val width = mAStarNormalBitmap?.width!! * mAStarNumbers
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var isStartDrawNormalFlog: Boolean = true


        repeat(mAStarNumbers) { drawTimes ->


            if(mCurrentSelectStarNumber > drawTimes) { //因为从drawTimes可以为0，所以为<而不是<=
                var currentDrawingStarWidth = drawTimes * (mAStarNormalBitmap!!.width)
                canvas?.drawBitmap(mAStarSelectBitmap!!, currentDrawingStarWidth.toFloat(), 0F, null)

            } else {
                var currentDrawingStarWidth = drawTimes * (mAStarNormalBitmap!!.width)
                canvas?.drawBitmap(mAStarNormalBitmap!!, currentDrawingStarWidth.toFloat(), 0F, null)
            }
        }


        /*for(i in 0..mAStarNumbers){
            val x = i * mAStarNormalBitmap?.width!!
            canvas?.drawBitmap(mAStarNormalBitmap!!, x.toFloat(), 0F, null)
        }*/


    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event?.action) {
            MotionEvent.ACTION_DOWN, //按下
            MotionEvent.ACTION_MOVE, //移动
            MotionEvent.ACTION_UP -> { //抬起


                val moveX = event.x //计算应该显示几颗星星
                var currentSelectStarNumbers: Int = (moveX / mAStarNormalBitmap?.width!! + 1).toInt() //当滑动位置超出星星最大数量的位置, 和为负的位置时
                if(currentSelectStarNumbers < 0) {
                    currentSelectStarNumbers = 0
                }
                if(currentSelectStarNumbers > mAStarNumbers) {
                    currentSelectStarNumbers = mAStarNumbers
                }

                //尽量减少不必要的绘制, 当在同一个星星上面滑动时候, 不应该再绘制了
                if(currentSelectStarNumbers == mCurrentSelectStarNumber){
                    return true
                }



                mCurrentSelectStarNumber = currentSelectStarNumbers
                invalidate()

            }
        }
        return true
    }



}