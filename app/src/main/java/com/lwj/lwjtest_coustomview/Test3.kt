package com.lwj.lwjtest_coustomview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent
import com.lwj.lwjtest_coustomview.MViewPager

/**
 * 自定义ViewPager
 * @author CXX
 */
internal class MViewPager(context: Context?, attrs: AttributeSet?) : ViewPager(context!!, attrs) {
    private var mDownX = 0f
    private var mDownY = 0f
    private var currState: Int
    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(arg0)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.x
                mDownY = ev.y
                Log.d("CXX", "当前状态Down$currState")
            }
            MotionEvent.ACTION_MOVE -> if(currState == NONE) {
                Log.d("CXX", "还在判定中")
                val dis = Math.sqrt(Math.pow((ev.x - mDownX).toDouble(), 2.0) + Math.pow((ev.y - mDownY).toDouble(), 2.0))
                    .toInt()
                if(dis > MINDIS) {
                    currState = if(Math.abs(ev.x - mDownX) > Math.abs(ev.y - mDownY)) {
                        // 横向移动
                        Log.d("CXX", "当前状态heng$currState")
                        parent.requestDisallowInterceptTouchEvent(true)
                        HORIZONTAL
                    } else {
                        // 竖直移动
                        Log.d("CXX", "当前状态shu$currState")
                        parent.requestDisallowInterceptTouchEvent(false)
                        VERTICAL
                    }
                }
            }
            MotionEvent.ACTION_UP -> currState = NONE
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return super.dispatchTouchEvent(ev)
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
}