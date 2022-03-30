package com.lwj.lwjtest_coustomview

import android.view.KeyEvent
import com.example.lwj_common.common.ui.controll.dialog.BaseDialogFragment
import com.lwj.lwjtest_coustomview_testview.databinding.DialogHomeBinding

class HomeDialog: BaseDialogFragment<DialogHomeBinding>() {
    override fun isCancel(): Boolean = true

    override fun DialogHomeBinding.initView() {

    }

}