package com.lwj.lwjtest_coustomview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import com.example.oinkredito.base.ui.controll.activity.BaseDbActivity
import com.lwj.lwjtest_coustomview_testview.databinding.ActivityMainBinding

class MainActivity : BaseDbActivity<ActivityMainBinding>(){
    override fun observe() {

    }

    override fun ActivityMainBinding.initView() {
        binding.cvQqstep.setMaxStep(10000)

        //属性动画
        val valueAnimator: ValueAnimator = ObjectAnimator.ofFloat(0F, 3000F)
        valueAnimator.duration = 1000
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener {
            val currentStep: Float = it.animatedValue as Float
            cvQqstep.setCurrentStep(currentStep.toInt())
        }
        valueAnimator.start()
    }

}