package com.lwj.lwjtest_coustomview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class SlidingConflictNestScrollView: NestedScrollView {
    private var mDownX = 0f
    private var mDownY = 0f
    //endregion
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr!!, 0)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, 0) {
    }

    //当该控件为viewgroup，即有孩子的时候，才有这个方法，用来专门针对是否拦截孩子的touch事件
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.x
                mDownY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = ev.x
                val moveY = ev.y
                if(Math.abs(moveX- mDownX) < Math.abs(moveY- mDownY)){
                    return true
                }
                /*
                //注意这种写法的话，当竖直滑动的起始触摸位置在bar上，无法竖直拖动
                if(Math.abs(moveX- mDownX) > Math.abs(moveY- mDownY)){
                    return false
                }*/
            }
            MotionEvent.ACTION_UP -> {}
            MotionEvent.ACTION_CANCEL -> {}
        }
        return super.onInterceptTouchEvent(ev)

    }

}