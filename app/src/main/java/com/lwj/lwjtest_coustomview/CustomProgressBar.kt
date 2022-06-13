package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.lwj.lwjtest_coustomview_testview.R

class CustomProgressBar : View {
    var mBottomTextHeight: Int = dp2px(40)

    private var pTextPaint: Paint? = null
    private var aTextColor: Int = 0
    var needToReduceGap: Float = 0F
    private var isMove: Boolean = false
    var aProgress: Int = 60


    //region 自定义view相关
    private var mHeight: Int = 0
    private var mWidth: Int = 0
    var paddingStartGap: Int = dp2px(20)
    var paddingEndGap: Int = dp2px(20)
    //endregion


    //region bar相关
    var mBarHeightProportionOnCircle: Float = 1 / 2F
    var aBarColor: Int = 0
    var aBarBgColor: Int = 0
    var aCircleWidth: Int = 0
    private var mBarHeight: Float = 0F
    private var mBarWidth: Float = 0F
    private var mBarStartDrawXPos: Int = 0
    private var mBarEndDrawXPos: Float = 0F
    private var mBarStartDrawYPos: Float = 0F
    private var mBarEndDrawYPos: Float = 0F

    //endregion

    //region circle相关
    private var aCircleRingSolidColor: Int = 0
    private val mCircleRingWidth: Float = dp2px(2).toFloat()
    private var mCircleHeight: Float = 0F
    var mCircleCounts: Int = 4
    var mHorCircleGapSize: Int = 0
    //endregion

    //region paint
    var pCircleSolidPaint: Paint? = null
    var pCirclePreviewPaint: Paint? = null
    var pCircleBgPaint: Paint? = null
    var pBarPaint: Paint? = null
    var pBarBgPaint: Paint? = null
    var pCircleRingPaint: Paint? = null
    //endregion
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr!!, 0)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, 0) {
        initAttrs(context, attr)
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var mWidth = MeasureSpec.getSize(widthMeasureSpec)
        var mHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(mWidth, if(mHeight > aCircleWidth + paddingBottom + paddingTop + mBottomTextHeight) aCircleWidth + paddingBottom + paddingTop + mBottomTextHeight else mHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mHeight = measuredHeight
        mWidth = measuredWidth

        mHorCircleGapSize = (width - paddingStart - paddingEnd - paddingEndGap - paddingStartGap) / (mCircleCounts - 1)
        mCircleHeight = (height - paddingTop - paddingBottom - mBottomTextHeight).toFloat()
        mBarHeight = mCircleHeight * mBarHeightProportionOnCircle

        needToReduceGap = (paddingStart + paddingEnd + paddingEndGap + paddingStartGap - aCircleWidth)/2F

        Log.d("---needToAddPerc", needToReduceGap.toString())

        //        mBarStartDrawXPos = paddingStart + paddingStartGap
        mBarStartDrawYPos = mCircleHeight / 2 - mBarHeight / 2
        mBarEndDrawXPos = (width - paddingStart - paddingEnd - paddingEndGap - paddingStartGap).toFloat()
        mBarEndDrawYPos = mBarStartDrawYPos + mBarHeight

    }

    fun Int.getCirclePreviewCount(): Int {
        when(this.toFloat()) {
            in 0F..33.3F -> return 1
            in 33.3F..66.6F -> return 2
            in 66.6F..99.9F -> return 3
            in 99.9F..100F -> return 4
            else -> return 1
        }
    }

    fun Int.getBarDrawXPos(): Float {
        when(this.toFloat()) {
            in 0F..16.6F  -> return 0F * mHorCircleGapSize
            in 16.6F ..49.9F  -> return 1F * mHorCircleGapSize
            in 49.9F ..83.2F  -> return 2F * mHorCircleGapSize
            in 83.2F ..100F -> return 3F * mHorCircleGapSize
            else -> return 0F

        }
    }

    fun Int.getTextDrawXIndex(): Int {
        when(this.toFloat()) {
            in 0F..16.6F  -> return 0
            in 16.6F ..49.9F  -> return 1
            in 49.9F ..83.2F  -> return 2
            in 83.2F ..100F -> return 3
            else -> return 0

        }
    }

    fun Int.getBarMoveDrawXPos(): Float= this/100F * mBarEndDrawXPos


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Log.d("---", "================================")
        drawBgCircle(canvas)
        drawBgBar(canvas)

        drawBar(canvas)
        drawCircle(canvas)

        drawText(canvas)
    }

    fun Int.getTextContent(): String=
        when(this){
        0 -> "0%"
        1 -> "30%"
        2 -> "60%"
        3 -> "100%"
        else -> "0%"}


    private fun drawText(canvas: Canvas?) {

        canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
            repeat(4) {
                if(it == aProgress.getTextDrawXIndex()){
                    val textWidth = pTextPaint?.measureText(it.getTextContent())

                    pTextPaint?.color = aTextColor
                    drawProgressText(it.getTextContent(), (paddingStartGap.toFloat() + it * mHorCircleGapSize - textWidth!!/2).toInt(), (mHeight - mBottomTextHeight/2).toFloat())
                }else{
                    val textWidth = pTextPaint?.measureText(it.getTextContent())
                    pTextPaint?.color = Color.WHITE
                    drawProgressText(it.getTextContent(), (paddingStartGap.toFloat() + it * mHorCircleGapSize - textWidth!!/2).toInt(), (mHeight - mBottomTextHeight/2).toFloat())
                }

            }
        }

    }

    private fun drawCircle(canvas: Canvas?) {
        drawCirclePreview(canvas)
        drawCircleSolid(canvas)
        drawCircleRing(canvas)
    }

    fun setMotionProgress(event: MotionEvent?){
        val currentProcess: Int = (event?.x!!.toInt()).toInt() - paddingStartGap - paddingStart
        if(currentProcess <= paddingStartGap + paddingStart ){
            aProgress = 0
        }else if(currentProcess >= (mWidth - paddingEndGap - paddingEnd - paddingStartGap - paddingStart)){
            aProgress = 100
        }else{
            aProgress = ((currentProcess.toFloat() / (mWidth - paddingEndGap - paddingEnd - paddingStartGap - paddingStart)) * 100).toInt()

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> { }

            MotionEvent.ACTION_MOVE -> {
                isMove = true
                setMotionProgress(event)
                parent.requestDisallowInterceptTouchEvent(true)
                Log.d("---event.x_move", event.x.toInt().toString())
                postInvalidate()

            }
            MotionEvent.ACTION_UP -> {
                isMove = false
                setMotionProgress(event)
                postInvalidate()
            }
        }
        return true

    }





    private fun drawCirclePreview(canvas: Canvas?) {
        repeat(aProgress.getCirclePreviewCount()){
            canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
                drawCircle(paddingStartGap.toFloat() + it * mHorCircleGapSize, (mHeight - mBottomTextHeight) / 2F, aCircleWidth / 2F, pCirclePreviewPaint!!)
            }
        }
    }

    private fun drawCircleSolid(canvas: Canvas?) {
        if(isMove){
            canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
                drawCircle(paddingStartGap.toFloat() + aProgress.getBarMoveDrawXPos(), (mHeight - mBottomTextHeight) / 2F, (aCircleWidth / 2F - mCircleRingWidth), pCircleSolidPaint!!)
            }
        }else{
            canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
                drawCircle(paddingStartGap.toFloat() + aProgress.getBarDrawXPos(), (mHeight - mBottomTextHeight) / 2F, (aCircleWidth / 2F - mCircleRingWidth), pCircleSolidPaint!!)
            }
        }
    }


    private fun drawCircleRing(canvas: Canvas?) {

        if(isMove){
            Log.d("---", "进来了drawCircleRing...")
            canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
                drawCircle(paddingStartGap.toFloat() + aProgress.getBarMoveDrawXPos(), (mHeight - mBottomTextHeight) / 2F, (aCircleWidth / 2F - mCircleRingWidth), pCircleSolidPaint!!)
            }
        }else{
            canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
                drawCircle(paddingStartGap.toFloat() + aProgress.getBarDrawXPos(), (mHeight - mBottomTextHeight) / 2F, (aCircleWidth / 2F - mCircleRingWidth / 2) , pCircleRingPaint!!)
            }
        }

    }

    fun Canvas.drawProgressText(text: String, textStartDrawX: Int, y: Float){
        //param: 绘制文字的左边起始位置(即y轴线最中间位置减去中间文本的一半长度就是左边起始位置)
        val textBounds = Rect()
        pTextPaint?.getTextBounds(text, 0, text.length, textBounds)
        val textHeight = textBounds.height()
        val startDrawX: Int = textStartDrawX
        //param: 文字的基线
        val fm: Paint.FontMetricsInt = pTextPaint!!.fontMetricsInt
        val baseLine = y + (fm.bottom - fm.top) / 2 - fm.bottom
        this.drawText(text, textStartDrawX.toFloat(), baseLine, pTextPaint!!)
    }

    private fun drawBar(canvas: Canvas?) {
        if(isMove){
            Log.d("---", "进来了drawBar...")
            canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
               // drawCircle(paddingStartGap.toFloat() + aProgress.getBarMoveDrawXPos(), mHeight / 2F, (aCircleWidth / 2F - mCircleRingWidth), pCircleSolidPaint!!)
                drawRect(paddingStartGap.toFloat(), mBarStartDrawYPos.toFloat(), paddingStartGap.toFloat() + aProgress.getBarMoveDrawXPos()
                    .toFloat(), mBarEndDrawYPos.toFloat(), pBarPaint!!)
            }
        }else{
            canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
                drawRect(paddingStartGap.toFloat(), mBarStartDrawYPos.toFloat(), paddingStartGap.toFloat() + aProgress.getBarDrawXPos()
                    .toFloat(), mBarEndDrawYPos.toFloat(), pBarPaint!!)
            }
        }



    }

    private fun drawBgBar(canvas: Canvas?) {
        canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
            drawRect(paddingStartGap.toFloat(), mBarStartDrawYPos.toFloat(), paddingStartGap.toFloat() + mBarEndDrawXPos.toFloat(), mBarEndDrawYPos.toFloat(), pBarBgPaint!!)
        }

    }

    private fun drawBgCircle(canvas: Canvas?) {
        canvas?.withTranslation(paddingStart.toFloat(), paddingTop.toFloat()) {
            repeat(4) {
                drawCircle(paddingStartGap.toFloat() + it * mHorCircleGapSize, (mHeight - mBottomTextHeight) / 2F, aCircleWidth / 2F, pCircleBgPaint!!)
            }
        }
    }

    private fun initAttrs(context: Context, attr: AttributeSet) {
        //模板代码
        val array: TypedArray = context.obtainStyledAttributes(attr, R.styleable.CustomProgressBar)

        aProgress = array.getInt(R.styleable.CustomProgressBar_progress, 80)
        aBarColor = array.getColor(R.styleable.CustomProgressBar_bar, ContextCompat.getColor(context, R.color.color_1296DB))
        aCircleRingSolidColor = array.getColor(R.styleable.CustomProgressBar_bar, ContextCompat.getColor(context, R.color.cl_app_theme))
        aBarBgColor = array.getColor(R.styleable.LwjQQStepView_arcInnerColor, ContextCompat.getColor(context, R.color.color_969696))
        aTextColor = array.getColor(R.styleable.CustomProgressBar_text_color, ContextCompat.getColor(context, R.color.color_1296DB))
        aCircleWidth = array.getDimensionPixelSize(R.styleable.CustomProgressBar_circle_height, dp2px(35))

        //模板代码
        array.recycle()
    }

    private fun sp2px(spValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue.toFloat(), resources.displayMetrics)
            .toInt()
    }

    private fun initPaint() {

        //押注白色文本字体
        pTextPaint = Paint()
        pTextPaint?.run {
            isAntiAlias = true
            color = Color.WHITE
            textSize = sp2px(14).toFloat()
        }

        pCirclePreviewPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pCirclePreviewPaint?.color = aBarColor
        pCirclePreviewPaint?.style = Paint.Style.FILL


        pCircleRingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pCircleRingPaint?.color = aBarColor
        pCircleRingPaint?.style = Paint.Style.STROKE
        pCircleRingPaint?.strokeWidth = mCircleRingWidth


        pCircleSolidPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pCircleSolidPaint?.color = aCircleRingSolidColor
        pCircleSolidPaint?.style = Paint.Style.FILL


        pCircleBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pCircleBgPaint?.color = aBarBgColor
        pCircleBgPaint?.style = Paint.Style.FILL

        pBarBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pBarBgPaint?.color = aBarBgColor
        pBarBgPaint?.style = Paint.Style.FILL


        pBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pBarPaint?.color = aBarColor
        pBarPaint?.style = Paint.Style.FILL
    }

    fun dp2px(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5).toInt()
    }
}