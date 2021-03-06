package com.lwj.lwjtest_coustomview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withTranslation
import com.example.lwj_common.common.ui.controll.tools.utils.DateUtil
import com.example.lwj_common.common.ui.controll.tools.utils.NumberUtils
import com.lwj.lwjtest_coustomview_testview.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LwjLineChartView : View {
    private var pStakeTrianglePaint: Paint? = null

    //region 押注
    private val dismissTime = 10
    private var isDrawStake: Boolean = true


    //var moveFrequencyCount: String? = null
    private var mAllStakesOffMap: HashMap<String, Any?> = HashMap()
    var valuePercentLast: Float = 0F
    //endregion


    private var mStartDrawFirstVerRulerGapSize: Float = 0F
    private var mTableLeftMovePointGapSize: Float = 0F

    //region 自定义view相关
    var aBgColor: Int = Color.BLACK //自定义view背景颜色

    //自定义view的宽高
    private var mHeight: Int = 0
    private var mWidth: Int = 0
    //endregion

    //region 绘制的表格相关
    private val mPrecisionEstimate: Int = 20
    private var mTopDisplaySpace = dp2px(50)
    private var mTableHeight: Int = 0
    private var mTableWidth: Int = 0
    private var mTimeTextHeight: Int = 0
    private var mValueTextHeight: Int = 0
    private var mValueTextWidth: Int = 0
    val mPaddingBoottomTextToTable: Int = dp2px(22) //底部文字最下方x坐标距离表格的距离
    val mPaddingEndToTable: Int = dp2px(80) //view的右边界距离表格的距离
    val mPaddingRightTextToTable: Int = dp2px(30) //右边文字距离表格的距离
    var aRulerColor: Int = Color.WHITE //标尺颜色
    var aRulerLineSize: Int = 2
    var mHorRulerGapSize: Int = 0 //y轴横线标尺间隔 sp2dp
    var aHorRulerCountNum: Int = 11 //y轴标尺数量
    var aVerRulerCountNum: Int = 4 //x轴标尺数量
    var aDefaultVerRulerCountNum: Int = 4 //默认一小时x轴标尺数量
    var mVerRulerGapSize: Float = 0F //竖直标尺间隙
    // endregion

    //region 折线相关
    private var pointNumber = 1800 //折线点的个数
    private var aXPointInitGapSize: Float = 0F
    var mInitLeftMoveX: Float = 70F
    private var path: Path = Path()
    var aLineChartColor: Int = Color.BLUE //折线颜色
    var aXPointGapSize: Float = 2F //折线X轴每个坐标点横坐标间的距离 sp2dp


    var mTimeType: Int = 60
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }
    //endregion

    //region 接入的数据
    var valueDatas: ArrayList<String> = arrayListOf()
    var timeDatas: ArrayList<String> = arrayListOf() //我需要的time数据
    var timeAllLongDatas: ArrayList<Long> = arrayListOf() //从websocket获取的time数据
    var lineChartDatas: ArrayList<Double> = arrayListOf()
    //endregion

    //region paint相关
    var pStakeOvalPaint: Paint? = null
    var pStakeTextPaint: Paint? = null
    var pStakeGraphicPaint: Paint? = null
    var pRulerPaint: Paint? = null
    var pLineChartPaint: Paint? = null
    var pXAxisTimePaint: Paint? = null
    var pYAxisValuePaint: Paint? = null
    var pShadowPaint: Paint? = null
    var pCurrentPointDashPaint: Paint? = null
    var pCurrentPointLinePaint: Paint? = null
    var pCurrentPointOutCirclePaint: Paint? = null
    var pCurrentPointInnerCirclePaint: Paint? = null
    var pCurrentPointTextPaint: Paint? = null
    var pCurrentPointTextBgPaint: Paint? = null
    //endregion

    //region 当前绘制点相关, 如显示当前值、显示虚线等
    private val mOutRingWidth: Float = 4F
    private val mInnerRadius: Float = 6F
    private val mOutRadius: Float = 8F

    //文本椭圆背景左右padding
    var mCurrentPointTextBgHorPadding: Int = sp2px(6)

    //文本椭圆背景宽度
    var mCurrentPointTextBgWidth: Float = 0F

    //当滑动距离使得文本椭圆背景不能显示完全时候, 文本椭圆背景应该绘制的起始点坐标
    var mCurrentPointTextBgCriticalStartX: Float = 0F

    //当前点右边绘制的线的宽度
    var mCurrentPointRightLineWidth: Float = 22F

    //虚线参考画笔颜色值
    var aCurrentPointDashColor: Int = Color.WHITE

    //当前点横线颜色值
    var aCurrentPointLineColor: Int = Color.WHITE

    //当前点外圆
    var aCurrentPointOutCircleColor: Int = Color.WHITE

    //当前点内圆
    var aCurrentPointInnerCircleColor: Int = Color.BLUE

    //当前点文本颜色值
    var aCurrentPointTextColor: Int = Color.WHITE

    //当前点文本背景
    var aCurrentPointTextBgColor: Int = Color.WHITE

    //椭圆文本起点
    var mCurrentPointTextBgStartX: Float = 0F

    //绘制的当前点文本/或椭圆背景的宽度
    var mCurrentPointTextWidth: Float = 0F

    //绘制的当前点文本高度
    var mCurrentPointTextHeight: Float = 0F

    //当前点文本大小
    var aCurrentPointTextSize: Int = 11 //sp2dp
    //endregion

    //region x、y轴文本相关
    //x轴时间文本颜色和大小
    var aXAxisTextColor: Int = Color.WHITE
    var aXAxisTextSize: Int = 20 //sp2dp

    //y轴时间文本颜色和大小
    var aYAxisTextColor: Int = Color.WHITE
    var aYAxisTextSize: Int = 20
    //endregion

    //region 滑动相关
    //最开始绘制页面时候左边移动的距离
    private var mMoveX: Float = 0F

    //记录存档最开始绘制页面时候左边移动的距离
    private var mInitMoveX: Float = 0F

    //折线滑动的距离
    private var mOffX: Float = 0F

    //滑动误操作范围
    private var mMoveFailX: Float = 300F

    //滑动开始按下的x,y
    private var mLastDownX: Float = 0F
    private var mLastDownY: Float = 0F
    //endregion

    //region 折线记录的点坐标
    //折线第一个和最后一个点的y、x坐标原点(0,0)
    private var lastY: Float = 0F
    private var lastX: Float = 0F
    //endregion

    fun getStakeDatas(): HashMap<String, Any?> {
        return mAllStakesOffMap
    }

    fun setStakeFromSaveInstance(map: HashMap<String, Any?>) {
        mAllStakesOffMap = map
        postInvalidate()
    }

    fun setDatas(lineChartList: ArrayList<Double>, timeTempList: ArrayList<Long>, isRealTime: Boolean = false) {
        lineChartDatas = lineChartList
        timeAllLongDatas = timeTempList
        if(isRealTime) {
            mTableLeftMovePointGapSize += aXPointGapSize
            if(mTableLeftMovePointGapSize >= mVerRulerGapSize) {
                mTableLeftMovePointGapSize = 0F
            }
            if(!mAllStakesOffMap.isNullOrEmpty()) {
                val iterator = mAllStakesOffMap.entries.iterator()
                while(iterator.hasNext()) {
                    val entry: MutableMap.MutableEntry<String, Any?> = iterator.next()
                    if(entry.value is Int) {
                        if(entry.value == dismissTime) {
                            iterator.remove();
                        } else {
                            entry.setValue((entry.value as Int) + 1)
                        }
                    }
                }
            }
        }
        postInvalidate()

    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet!!, 0)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        //initValue()
        initAttrs(context, attributeSet) //注意这里一定要先初始化属性, 再初始化画笔
        initPaints()
    }


    private fun drawCurrentPointTextBg(canvas: Canvas) {
        mCurrentPointTextWidth = lineChartDatas?.get(lineChartDatas?.size!! - 1)?.toString()
            ?.getDrawWidth(pCurrentPointTextPaint!!)?.toFloat()!!
        mCurrentPointTextHeight = lineChartDatas?.get(lineChartDatas?.size!! - 1)?.toString()
            ?.getDrawWidth(pCurrentPointTextPaint!!)?.toFloat()!!
        mCurrentPointTextBgWidth = mCurrentPointTextWidth + 2 * mCurrentPointTextBgHorPadding
        mCurrentPointTextBgCriticalStartX = mWidth - mCurrentPointTextBgWidth //view/屏幕宽度 - 椭圆文本背景宽度 = 当拖拽距离过大导致椭圆背景显示不全时候的 椭圆背景开始绘制的起始坐标
        if(mCurrentPointTextBgStartX > mCurrentPointTextBgCriticalStartX) {
            mCurrentPointTextBgStartX = mCurrentPointTextBgCriticalStartX
        }
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            val rect: RectF = RectF(mCurrentPointTextBgStartX, lastY - mCurrentPointTextHeight / 4, mCurrentPointTextBgStartX + mCurrentPointTextBgWidth, lastY + mCurrentPointTextHeight / 4)
            drawRoundRect(rect, 50F, 50F, pCurrentPointTextBgPaint!!)
        }
    }

    private fun drawCurrentPointText(canvas: Canvas) {
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            drawTextBase(lineChartDatas?.get(lineChartDatas?.size!! - 1)
                .toString(), mCurrentPointTextBgStartX + mCurrentPointTextBgWidth / 2 - mCurrentPointTextWidth / 2, lastY, pCurrentPointTextPaint!!)
        }

    }


    private fun drawCurrentPointLine(canvas: Canvas) {
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                drawLine(0F, lastY, lastX - mInnerRadius - mOutRingWidth, lastY, pCurrentPointLinePaint!!)
                drawLine(lastX + mInnerRadius + mOutRingWidth, lastY, mCurrentPointTextBgStartX, lastY, pCurrentPointLinePaint!!)
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                drawLine(0F - mMoveX, lastY, lastX - mInnerRadius - mOutRingWidth - mMoveX, lastY, pCurrentPointLinePaint!!)
                drawLine(lastX + mInnerRadius + mOutRingWidth - mMoveX, lastY, mCurrentPointTextBgStartX, lastY, pCurrentPointLinePaint!!)
            }
        }
    }

    private fun drawCurrentPointOutCircle(canvas: Canvas) {
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                drawCircle(lastX, lastY, mOutRadius, pCurrentPointOutCirclePaint!!)
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                drawCircle(lastX - mMoveX, lastY, mOutRadius, pCurrentPointOutCirclePaint!!)
            }

        }
    }


    private fun drawCurrentPointInnerCircle(canvas: Canvas) {
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                drawCircle(lastX, lastY, mInnerRadius, pCurrentPointInnerCirclePaint!!)
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                drawCircle(lastX - mMoveX, lastY, mInnerRadius, pCurrentPointInnerCirclePaint!!)
            }
        }
    }

    private fun drawCurrentPointDash(canvas: Canvas) {
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                drawLine(lastX, 0F, lastX, mTableHeight.toFloat(), pCurrentPointDashPaint!!)
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                drawLine(lastX - mMoveX, 0F, lastX - mMoveX, mTableHeight.toFloat(), pCurrentPointDashPaint!!)
            }
        }
    }

    //绘制折线和背景
    private fun drawLineChart(canvas: Canvas, list: ArrayList<Double>) {
        //path需要重置, 否则会导致两次折线重叠
        path.reset()
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            //折线第一个点
            val valuePercentFirst = (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(list[0].toString())) / (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(valueDatas[valueDatas.count() - 1]))
            val firstY = valuePercentFirst * mTableHeight
            path.moveTo(0F, firstY)
            //折线最后一个点x和y
            valuePercentLast = (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(list[lineChartDatas?.count()!! - 1].toString())) / (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(valueDatas[valueDatas.count() - 1]))
            lastY = valuePercentLast * mTableHeight
            lastX = (list.count() - 1) * aXPointGapSize.toFloat()

            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                mCurrentPointTextBgStartX = lastX + mInnerRadius + mOutRingWidth + mCurrentPointRightLineWidth
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                mCurrentPointTextBgStartX = lastX + mInnerRadius + mOutRingWidth + mCurrentPointRightLineWidth - mMoveX
            }
            //折线中间点的y
            var valueYProgress = 0F

            //折线中间点的y
            var valueXProgress = 0F

            var linearGradient: LinearGradient = LinearGradient(0F, 0F, 0F, mTableHeight.toFloat(), intArrayOf(Color.parseColor("#99BAEFE6"), Color.parseColor("#99D7F5F0"), Color.parseColor("#99F9FEFD")), floatArrayOf(0.5f, 0.65f, 0.85f), Shader.TileMode.REPEAT)


            //折线中间的点以及最后的点
            repeat(list.count() - 1) {
                val valuePercent = (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(list[it + 1].toString())) / (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(valueDatas[valueDatas.count() - 1]))
                valueYProgress = valuePercent * mTableHeight
                valueXProgress = (it + 1) * aXPointGapSize.toFloat()
                path.lineTo(valueXProgress, valueYProgress)

            }


            pShadowPaint?.shader = linearGradient
            if(list.count() * aXPointGapSize / mTableWidth < 2F) {
                //画线
                drawPath(path, pLineChartPaint!!)
                //画背景
                path.lineTo(lastX, mTableHeight.toFloat())
                path.lineTo(0F, mTableHeight.toFloat())
                path.close()
                drawLineChartBg(this, path)
            } else if(list.count() * aXPointGapSize / mTableWidth > 2F) {
                path.offset(-mMoveX, 0F)
                //画线
                drawPath(path, pLineChartPaint!!)
                //画背景
                path.lineTo(lastX - mMoveX, mTableHeight.toFloat())
                path.lineTo(0F - mMoveX, mTableHeight.toFloat())
                path.close()
                drawLineChartBg(this, path)
            }
        }
    }


    private fun drawLineChartBg(canvas: Canvas, path: Path) {
        canvas.drawPath(path, pShadowPaint!!)
    }

    private fun drawYAxisText(canvas: Canvas) {
        repeat(aHorRulerCountNum) {
            canvas.withTranslation((width - mPaddingEndToTable + mPaddingRightTextToTable).toFloat(), (it * mHorRulerGapSize + paddingTop).toFloat() + mTopDisplaySpace) {
                drawValueTextBase(it, mHorRulerGapSize / 10)
            }
        }
    }

    private fun drawXAxisText(canvas: Canvas) {
        var y = mPaddingBoottomTextToTable / 2
        canvas.withTranslation(paddingLeft.toFloat(), ((aHorRulerCountNum - 1) * mHorRulerGapSize + mPaddingBoottomTextToTable / 4 + mTopDisplaySpace).toFloat()) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                repeat(aVerRulerCountNum) {
                    if(it == 0) {
                        drawTimeTextBase(true, it, (0F + mVerRulerGapSize * it - mTableLeftMovePointGapSize).toInt(), y)
                    } else {
                        drawTimeTextBase(false, it, (0F + mVerRulerGapSize * it - mTableLeftMovePointGapSize).toInt(), y)
                    }
                }
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                var i = (lineChartDatas?.count()!! * aXPointGapSize) / mVerRulerGapSize
                repeat(aVerRulerCountNum) {
                    if(it == 0) {
                        drawTimeTextBase(true, it, (0F + mVerRulerGapSize * it - mMoveX - mTableLeftMovePointGapSize).toInt(), y)
                    } else {
                        drawTimeTextBase(false, it, (0F + mVerRulerGapSize * it - mMoveX - mTableLeftMovePointGapSize).toInt(), y)
                    }
                }
            }
        }
    }

    private fun drawTableRuler(canvas: Canvas) {
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                repeat(aHorRulerCountNum) {
                    canvas.drawLine(0F, (it * mHorRulerGapSize).toFloat(), (mVerRulerGapSize * (aVerRulerCountNum - 1)).toFloat(), (it * mHorRulerGapSize).toFloat(), pRulerPaint!!)
                }
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                repeat(aHorRulerCountNum) {
                    canvas.drawLine(0F, (it * mHorRulerGapSize).toFloat(), mWidth.toFloat(), (it * mHorRulerGapSize).toFloat(), pRulerPaint!!)
                }
            }
        }

        //绘制竖线
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                repeat(aVerRulerCountNum) {
                    canvas.drawLine(0F - mTableLeftMovePointGapSize + (it * mVerRulerGapSize).toFloat(), 0F, (it * mVerRulerGapSize).toFloat() - mTableLeftMovePointGapSize, ((aHorRulerCountNum - 1) * mHorRulerGapSize).toFloat(), pRulerPaint!!)
                }
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                repeat(aVerRulerCountNum) {
                    canvas.drawLine((it * mVerRulerGapSize).toFloat() - mMoveX - mTableLeftMovePointGapSize, 0F, (it * mVerRulerGapSize).toFloat() - mMoveX - mTableLeftMovePointGapSize, ((aHorRulerCountNum - 1) * mHorRulerGapSize).toFloat(), pRulerPaint!!)
                }
            }
        }
    }


    /**
     * @param dataIndex 集合里面数据的下标
     * @param y 绘制的y坐标
     * */
    private fun Canvas.drawValueTextBase(dataIndex: Int, y: Int) {
        val text = valueDatas[dataIndex] //param: 绘制文字的左边起始位置(即y轴线最中间位置减去中间文本的一半长度就是左边起始位置)
        val textBounds = Rect()
        pXAxisTimePaint?.getTextBounds(text, 0, text.length, textBounds)
        mValueTextHeight = textBounds.height()
        mValueTextWidth = textBounds.width()
        val startDrawX: Int = 0 //param: 文字的基线
        val fm: Paint.FontMetricsInt = pXAxisTimePaint!!.fontMetricsInt
        val baseLine = y + (fm.bottom - fm.top) / 2 - fm.bottom
        drawText(text, startDrawX.toFloat(), baseLine.toFloat(), pXAxisTimePaint!!)

    }

    /**
     * @param string 绘制的文本内容
     * @param startDrawX 绘制的文本起始坐标位置
     * @param y 绘制的文本高度的一半所对应的y轴坐标
     * @param paint 绘制文本的画笔
     * */
    private fun Canvas.drawTextBase(string: String, startDrawX: Float, y: Float, paint: Paint) {
        val fm: Paint.FontMetricsInt = paint.fontMetricsInt
        val baseLine = y + (fm.bottom - fm.top) / 2 - fm.bottom
        drawText(string, startDrawX, baseLine, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastDownX = event.x
                mLastDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                mOffX = mLastDownX - event.x
                mLastDownX = event.x
                mLastDownY = event.y
                if(mMoveX + mOffX > 0 && mMoveX + mOffX < mInitMoveX) {
                    mMoveX += mOffX
                } else if(mMoveX + mOffX <= 0) {
                    mMoveX = 0F
                } else if(mMoveX + mOffX >= mMoveFailX) {
                    mMoveX = mInitMoveX
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if(mInitMoveX - mMoveX > 5 && mInitMoveX - mMoveX < mMoveFailX) {
                    mMoveX = mInitMoveX
                    invalidate()
                }
            }
        }
        return true
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mHeight = measuredHeight
        mWidth = measuredWidth
        mHorRulerGapSize = (height - paddingTop - paddingBottom - mPaddingBoottomTextToTable - 2 * mTopDisplaySpace) / (aHorRulerCountNum - 1) //这个20是给默认底部文字留的空间
        mVerRulerGapSize = (width - paddingLeft - paddingRight - mPaddingEndToTable) / (aDefaultVerRulerCountNum - 1).toFloat() //这个50是给默认右边文字留的空间

        mTableHeight = mHorRulerGapSize * (aHorRulerCountNum - 1)
        mTableWidth = width - paddingLeft - paddingRight - mPaddingEndToTable

        aXPointInitGapSize = (width - paddingLeft - paddingRight - mPaddingEndToTable) / (pointNumber - 1).toFloat()
        when(mTimeType) {
            60 -> aXPointGapSize = aXPointInitGapSize
            30 -> aXPointGapSize = 2 * aXPointInitGapSize
            15 -> aXPointGapSize = 4 * aXPointInitGapSize
            3 -> aXPointGapSize = 20 * aXPointInitGapSize
        }
        if(pointNumber * aXPointGapSize / mTableWidth < 2F) {
            mMoveX = ((pointNumber - 1) * aXPointGapSize - mTableWidth).toFloat()
        } else if(pointNumber * aXPointGapSize / mTableWidth > 2F) {
            mMoveX = ((pointNumber - 1) * aXPointGapSize - mTableWidth).toFloat() + mInitLeftMoveX
        }

        mInitMoveX = mMoveX

        if(pointNumber * aXPointGapSize / mTableWidth < 2F) {
            aVerRulerCountNum = aDefaultVerRulerCountNum
        } else if(pointNumber * aXPointGapSize / mTableWidth > 2F) {
            aVerRulerCountNum = (((pointNumber - 1) * aXPointGapSize) / mVerRulerGapSize + 1).toInt()
        }
    }

    fun setStake(stakeColor: Int) {
        pStakeGraphicPaint?.color = stakeColor
        val moveFrequencyCount = UUID.randomUUID().toString()
        //注意这里的值不能从0开始
        mAllStakesOffMap[moveFrequencyCount] = 0
        //        mAllStakesOffMap["test"] = "ddd"
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(lineChartDatas.count() > 0 && timeAllLongDatas.count() > 0) {
            convertDataToNeed(lineChartDatas, timeAllLongDatas)
        }
        if(lineChartDatas.count() > 0 && timeAllLongDatas.count() > 0) {
            canvas?.drawColor(Color.BLACK)
            canvas?.run {
                drawTableRuler(this)
                drawXAxisText(this)
                drawYAxisText(this)
                drawLineChart(this, lineChartDatas)
                drawCurrentPointDash(this)
                drawCurrentPointInnerCircle(this)
                drawCurrentPointOutCircle(this)
                drawCurrentPointLine(this)
                drawCurrentPointTextBg(this)
                drawCurrentPointText(this)
                if(!mAllStakesOffMap.isNullOrEmpty()) {
                    drawStake(this)
                }
            }
        }

    }

    fun drawStakeTriangle(circleX: Float, circleY: Float, radius: Float) {
        var pathTrangle: Path = Path()
        pStakeTrianglePaint

    }

    private fun drawStake(canvas: Canvas) {
        if(mAllStakesOffMap.isNullOrEmpty()) return
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat() + mTopDisplaySpace) {
            if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth < 2F) {
                for((_, value) in mAllStakesOffMap) {
                    if(value is Int) {
                        val valueY = lineChartDatas[lineChartDatas.count() - 1 - value].toFloat()
                        val valuePercent = (NumberUtils.parseFloat(valueDatas[0]) - valueY) / (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(valueDatas[valueDatas.count() - 1]))
                        val valueYProgress = valuePercent * mTableHeight
                        canvas.drawCircle(0F - value * aXPointGapSize + (pointNumber - 1) * aXPointGapSize, valueYProgress, 5F, pStakeGraphicPaint!!)
                    }

                }
            } else if(lineChartDatas?.count()!! * aXPointGapSize / mTableWidth > 2F) {
                for((_, value) in mAllStakesOffMap) {
                    if(value is Int) {
                        val valueY = lineChartDatas[lineChartDatas.count() - 1 - value].toFloat()
                        val valuePercent = (NumberUtils.parseFloat(valueDatas[0]) - valueY) / (NumberUtils.parseFloat(valueDatas[0]) - NumberUtils.parseFloat(valueDatas[valueDatas.count() - 1]))
                        val valueYProgress = valuePercent * mTableHeight
                        canvas.drawCircle(-mMoveX - value * aXPointGapSize + (pointNumber - 1) * aXPointGapSize, valueYProgress, 5F, pStakeGraphicPaint!!)
                    }
                }
            }
        }


    }


    private fun convertDataToNeed(lineChartList: ArrayList<Double>, timeTempList: ArrayList<Long>) {
        lineChartDatasToValueDatas(lineChartList)
        timeAllLongDatasTotimeDatas(timeTempList)
    }


    private fun timeAllLongDatasTotimeDatas(timeTempAllList: ArrayList<Long>) {
        timeDatas.clear()
        //两根竖直尺子间存在的点的个数
        val pointCountVerRulerGap = pointNumber / (aVerRulerCountNum - 1)
        //        timeTempAllList.map { DateUtil.stampToDate(it) } as ArrayList<String>
        for(i in 0 until aVerRulerCountNum) {
            var verRulerIndex = 0
            if(i == 0) {
                verRulerIndex = 0
                timeDatas.add(0, "")
            } else {
                verRulerIndex = i * pointCountVerRulerGap - 1 - (mTableLeftMovePointGapSize / aXPointGapSize).ceil()
                if(verRulerIndex <= 0) {
                    verRulerIndex = 0
                }
                timeDatas.add(i, DateUtil.stampToDate(timeTempAllList[verRulerIndex])
                    .split(" ".toRegex()).get(1))
            }
        }
    }

    private fun lineChartDatasToValueDatas(lineChartList: ArrayList<Double>) {
        valueDatas.clear()
        var pointCountLeftMove: Float = mMoveX / aXPointGapSize + 1
        val lineChartInScreenDatas = lineChartList.subList(pointCountLeftMove.ceil(), 1800 /*lastIndex*/)
        val maxValue = lineChartInScreenDatas.maxOrNull()
        val minValue = lineChartInScreenDatas.minOrNull()
        val horRulerReduceValue = ((maxValue!! - minValue!!) / (aHorRulerCountNum - 1))
        for(i in 0 until aHorRulerCountNum) {
            val ele = maxValue - (horRulerReduceValue) * i
            valueDatas.add(ele.toString())
        }
    }

    //    -------------------------
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec) + mTopDisplaySpace
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, height)
    }

    fun dp2px(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5).toInt()
    }

    private fun sp2px(spValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue.toFloat(), resources.displayMetrics)
            .toInt()
    }

    private fun initPaints() {
        //押注三角形
        pStakeTrianglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pStakeTrianglePaint?.run {
            color = resources.getColor(R.color.top_white_tran)
            style = Paint.Style.FILL
        }

        //押注椭圆背景
        pStakeOvalPaint = Paint()
        pStakeOvalPaint?.run {
            isAntiAlias = true
            color = Color.GREEN
            style = Paint.Style.FILL_AND_STROKE
        }

        //押注白色文本字体
        pStakeTextPaint = Paint()
        pStakeTextPaint?.run {
            isAntiAlias = true
            color = Color.WHITE
            textSize = aCurrentPointTextSize.toFloat()
        }

        //押注圆图形
        pStakeGraphicPaint = Paint()
        pStakeGraphicPaint?.run {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = Color.GREEN
        }


        //标尺画笔
        pRulerPaint = Paint()
        pRulerPaint?.run {
            style = Paint.Style.STROKE
            strokeWidth = aRulerLineSize.toFloat()
            isAntiAlias = true
            isDither = true
            color = aRulerColor
        }

        //折线画笔
        pLineChartPaint = Paint()
        pLineChartPaint?.run {
            style = Paint.Style.STROKE
            strokeWidth = 1F
            isAntiAlias = true
            color = aLineChartColor
        }

        //背景画笔
        pShadowPaint = Paint()
        pShadowPaint?.run {
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
        }

        //底部文本画笔
        pXAxisTimePaint = Paint()
        pXAxisTimePaint?.run {
            isAntiAlias = true
            color = aXAxisTextColor
            textSize = aXAxisTextSize.toFloat()
        }

        //右侧文本画笔
        pYAxisValuePaint = Paint()
        pYAxisValuePaint?.run {
            isAntiAlias = true
            color = aYAxisTextColor
            textSize = aYAxisTextSize.toFloat()
        }

        //当前点虚线
        var array = floatArrayOf(4F, 4F) //第一个参数: 实现长度, 第二个参数: 虚线长度
        pCurrentPointDashPaint = Paint()
        pCurrentPointDashPaint?.run {
            isAntiAlias = true
            color = aCurrentPointDashColor
            style = Paint.Style.STROKE
            strokeWidth = 3F
            setPathEffect(DashPathEffect(array, 5F))
        }

        //当前点内圆
        pCurrentPointInnerCirclePaint = Paint()
        pCurrentPointInnerCirclePaint?.run {
            isAntiAlias = true
            color = aCurrentPointInnerCircleColor
            style = Paint.Style.FILL
        }

        //当前点外圆环
        pCurrentPointOutCirclePaint = Paint()
        pCurrentPointOutCirclePaint?.run {
            isAntiAlias = true
            color = aCurrentPointOutCircleColor
            style = Paint.Style.STROKE
            strokeWidth = mOutRingWidth
        }

        //当前点横线
        pCurrentPointLinePaint = Paint()
        pCurrentPointLinePaint?.run {
            isAntiAlias = true
            color = aCurrentPointLineColor
            style = Paint.Style.STROKE
            strokeWidth = 4F
        }

        //当前点文本
        pCurrentPointTextPaint = Paint()
        pCurrentPointTextPaint?.run {
            isAntiAlias = true
            color = Color.BLACK
            textSize = aCurrentPointTextSize.toFloat()
        }

        //当前点文本椭圆背景
        pCurrentPointTextBgPaint = Paint()
        pCurrentPointTextBgPaint?.run {
            isAntiAlias = true
            color = aCurrentPointTextBgColor
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    /**
     * @param index 数据index
     * @param canvas
     * @param xRulerPos y轴每一根标尺的x坐标
     * */
    private fun Canvas.drawTimeTextBase(isFirstElement: Boolean, dataIndex: Int, xRulerPos: Int, y: Int) {
        if(!isFirstElement) {
            //绘制的文本内容
            val text = timeDatas.getOrElse(dataIndex) { dataIndex -> "数据不够" }
            //param: 绘制文字的左边起始位置(即y轴线最中间位置减去中间文本的一半长度就是左边起始位置)
            val textBounds = Rect()
            pXAxisTimePaint?.getTextBounds(text, 0, text.length, textBounds)
            mTimeTextHeight = textBounds.height()
            val startDrawX: Int = xRulerPos - textBounds.width() / 2
            //param: 文字的基线
            val fm: Paint.FontMetricsInt = pXAxisTimePaint!!.fontMetricsInt
            val baseLine = y + (fm.bottom - fm.top) / 2 - fm.bottom
            drawText(text, startDrawX.toFloat(), baseLine.toFloat(), pXAxisTimePaint!!)
        } else {
            //绘制的文本内容
            val text = timeDatas.getOrElse(dataIndex) { dataIndex -> "数据不够" }
            //param: 绘制文字的左边起始位置(即y轴线最中间位置减去中间文本的一半长度就是左边起始位置)
            val textBounds = Rect()
            pXAxisTimePaint?.getTextBounds(text, 0, text.length, textBounds)
            mTimeTextHeight = textBounds.height()
            val startDrawX: Int = xRulerPos
            //param: 文字的基线
            val fm: Paint.FontMetricsInt = pXAxisTimePaint!!.fontMetricsInt
            val baseLine = y + (fm.bottom - fm.top) / 2 - fm.bottom
            drawText(text, startDrawX.toFloat(), baseLine.toFloat(), pXAxisTimePaint!!)
        }
    }

    private fun String.getDrawWidth(paint: Paint): Int {
        val textBounds = Rect()
        paint.getTextBounds(this, 0, this.length, textBounds)
        return textBounds.width()
    }

    private fun String.getDrawHeight(paint: Paint): Int {
        val textBounds = Rect()
        paint.getTextBounds(this, 0, this.length, textBounds)
        return textBounds.height()
    }

    private fun initAttrs(context: Context, attributeSet: AttributeSet) {
        var array: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LwjLineChartView)
        aBgColor = array.getColor(R.styleable.LwjLineChartView_bgColor, Color.BLACK)
        aRulerColor = array.getColor(R.styleable.LwjLineChartView_rulerColor, Color.WHITE)
        mHorRulerGapSize = array.getDimensionPixelSize(R.styleable.LwjLineChartView_yRulerGapSize, dp2px(35)) // aXRulerGapSize = array.getDimensionPixelSize(R.styleable.LwjLineChartView_xRulerGapSize, dp2px(120))
        aHorRulerCountNum = array.getInt(R.styleable.LwjLineChartView_yRulerCountNum, 11)
        aRulerLineSize = array.getDimensionPixelSize(R.styleable.LwjLineChartView_rulerLineSize, 2)
        aXAxisTextColor = array.getColor(R.styleable.LwjLineChartView_xAxisTextColor, Color.WHITE)
        aXAxisTextSize = array.getDimensionPixelSize(R.styleable.LwjLineChartView_xAxisTextSize, sp2px(18))
        aYAxisTextColor = array.getColor(R.styleable.LwjLineChartView_yAxisTextColor, Color.WHITE)
        aYAxisTextSize = array.getDimensionPixelSize(R.styleable.LwjLineChartView_yAxisTextSize, sp2px(18))
        aLineChartColor = array.getColor(R.styleable.LwjLineChartView_lineChartColor, Color.BLUE)
        aCurrentPointTextSize = array.getDimensionPixelSize(R.styleable.LwjLineChartView_currentPointTextSize, sp2px(11))
        aCurrentPointDashColor = array.getColor(R.styleable.LwjLineChartView_currentPointDashColor, Color.WHITE)
        aCurrentPointLineColor = array.getColor(R.styleable.LwjLineChartView_currentPointLineColor, Color.WHITE)
        aCurrentPointOutCircleColor = array.getColor(R.styleable.LwjLineChartView_currentPointOutCircleColor, Color.WHITE)
        aCurrentPointInnerCircleColor = array.getColor(R.styleable.LwjLineChartView_currentPointInnerCircleColor, Color.RED)
        aCurrentPointTextColor = array.getColor(R.styleable.LwjLineChartView_currentPointTextColor, Color.BLACK)
        aCurrentPointTextBgColor = array.getColor(R.styleable.LwjLineChartView_currentPointTextBgColor, Color.WHITE)
        array.recycle()
    }


    fun Float.ceil(): Int {
        return Math.ceil(this.toDouble()).toInt()
    }

    fun Double.ceil(): Int {
        return Math.ceil(this).toInt()
    }

    fun Int.isPartDisplay(): Boolean {
        when(this) {
            60 -> return false
            30 -> return false
            15 -> return false
            3 -> return true
            else -> return false
        }
    }


}