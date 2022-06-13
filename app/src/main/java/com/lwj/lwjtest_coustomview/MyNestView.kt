package com.lwj.lwjtest_coustomview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class MyNestView: NestedScrollView {

    //滑动开始按下的x,y
    private var mLastDownX: Float = 0F
    private var mLastDownY: Float = 0F
    //折线滑动的距离
    private var mOffX: Float = 0F

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr!!, 0)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, 0) {
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {

       /* when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastDownX = event.x
                mLastDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                mOffX = mLastDownX - event.x
                mLastDownX = event.x
                mLastDownY = event.y

                if()
            }
            MotionEvent.ACTION_UP -> {
                if(mInitMoveX - mMoveX > 5 && mInitMoveX - mMoveX < mMoveFailX) {
                    mMoveX = mInitMoveX
                    invalidate()
                }
            }
        }*/
        return super.onInterceptTouchEvent(event)
    }
}