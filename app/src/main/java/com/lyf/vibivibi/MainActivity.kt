package com.lyf.vibivibi

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lyf.vibi.library.util.StatusBar
import com.lyf.vibivibi.logic.MainActivityLogic
import kotlinx.android.synthetic.main.layout_main_page_toolbar.*


class MainActivity : AppCompatActivity(),MainActivityLogic.ActivityProvider{
    lateinit var activityLogic:MainActivityLogic
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityLogic = MainActivityLogic(this@MainActivity)
        StatusBar.setStatusBar(this, true, translucent = false)
    }

}