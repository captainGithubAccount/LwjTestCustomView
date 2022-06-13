package com.lwj.lwjtest_coustomview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class MyNestScrollView: NestedScrollView {
    private var currState: Int
    private var mDownX = 0f
    private var mDownY = 0f
    //endregion
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr!!, 0)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, 0) {
    }

    companion object {
        private const val NONE = 0
        private const val VERTICAL = 1
        private const val HORIZONTAL = 2
        private const val MINDIS = 30
    }

    init {
        currState = NONE
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.x
                mDownY = ev.y
                Log.d("CXX", "当前状态Down$currState")
            }
            MotionEvent.ACTION_MOVE -> if(currState == MyNestScrollView.NONE) {
                Log.d("CXX", "还在判定中")
                val dis = Math.sqrt(Math.pow((ev.x - mDownX).toDouble(), 2.0) + Math.pow((ev.y - mDownY).toDouble(), 2.0))
                    .toInt()
                if(dis > MyNestScrollView.MINDIS) {
                    currState = if(Math.abs(ev.x - mDownX) > Math.abs(ev.y - mDownY)) {
                        // 横向移动
                        Log.d("CXX", "当前状态heng$currState")
                        parent.requestDisallowInterceptTouchEvent(true)
                        MyNestScrollView.HORIZONTAL
                    } else {
                        // 竖直移动
                        Log.d("CXX", "当前状态shu$currState")
                        parent.requestDisallowInterceptTouchEvent(false)
                        MyNestScrollView.VERTICAL
                    }
                }
            }
            MotionEvent.ACTION_UP -> currState = MyNestScrollView.NONE
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}