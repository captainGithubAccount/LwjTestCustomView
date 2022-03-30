package com.lwj.lwjtest_coustomview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import com.example.oinkredito.base.ui.controll.activity.BaseDbActivity
import com.lwj.lwjtest_coustomview_testview.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : BaseDbActivity<ActivityMainBinding>(){
    override fun observe() {

    }

    override fun ActivityMainBinding.initView() {
        initQqStepView()
        initFontChangeColorView()
        initLoadingView()
    }

    private fun initLoadingView() {
        binding.tvLoadingStart.setOnClickListener{
            Thread {
                while(true) {
                    runOnUiThread {
                        binding.cvLoadingView.changeCurrentDrawing()
                    }
                    try {
                        Thread.sleep(1000)
                    } catch(e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
            /*val valueAnimator: ValueAnimator = ObjectAnimator.ofFloat(0F, 4000F)
            valueAnimator.duration = 4000 * 1000
            valueAnimator.addUpdateListener {
                binding.cvLoadingView.changeCurrentDrawing()
            }*/
        }
    }

    private fun initFontChangeColorView() {
        binding.btnLeftToRight.setOnClickListener {
            leftToRight()
        }

        binding.btnRightToLeft.setOnClickListener {
            rightToLeft()
        }
    }

    private fun initQqStepView() {
        binding.cvQqstep.setMaxStep(10000)

        //属性动画
        val valueAnimator: ValueAnimator = ObjectAnimator.ofFloat(0F, 3000F)
        valueAnimator.duration = 1000
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener {
            val currentStep: Float = it.animatedValue as Float
            binding.cvQqstep.setCurrentStep(currentStep.toInt())
        }
        valueAnimator.start()
    }

    fun leftToRight(){//从左向右滑，一开始是黑色, 随着滑动, 左边黑右边慢慢变蓝, 最终颜色为蓝色
        binding.cvFontChangeColor.mSwipeDirection = LwjFontChangeColorView.Direction.SWIPE_LEFT_TO_RIGHT
        val valueAnimator: ValueAnimator = ObjectAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener {
            val currentProcess: Any = it.animatedValue

            binding.cvFontChangeColor.mCurrentClipProgress = currentProcess as Float
        }
        valueAnimator.start()
    }

    fun rightToLeft(){//从右向左滑, 一开始是蓝色, 随着滑动, 左边蓝右边慢慢变黑, 最终变为黑色
        binding.cvFontChangeColor.mSwipeDirection = LwjFontChangeColorView.Direction.SWIPE_RIGHT_TO_LEFT
        val valueAnimator: ValueAnimator = ObjectAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener {
            val currentProcess: Any = it.animatedValue
            binding.cvFontChangeColor.mCurrentClipProgress = currentProcess as Float
        }
        valueAnimator.start()
    }

}