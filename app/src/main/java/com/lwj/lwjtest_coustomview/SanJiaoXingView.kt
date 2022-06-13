package com.lwj.lwjtest_coustomview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.lwj.lwjtest_coustomview_testview.R

class SanJiaoXingView: View {
    var mSanJiaoPaint: Paint? = null

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        initPaint(context)
    }

    private fun initPaint(context: Context) {
        mSanJiaoPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mSanJiaoPaint?.color = ContextCompat.getColor(context, R.color.white)
        mSanJiaoPaint?.style = Paint.Style.FILL
    }
}