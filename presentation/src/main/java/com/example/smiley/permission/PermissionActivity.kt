package com.example.smiley.permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.common.extension.setDisabled
import com.example.smiley.common.extension.setEnabled
import com.example.smiley.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {

    private lateinit var bind:ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_permission)
        initAllowBtn()
        addInfoCheckBoxClickEvent()
    }

    private fun initAllowBtn(){
        bind.allowBtn.setDisabled()
    }

    private fun addInfoCheckBoxClickEvent(){
        bind.infoCheckbox.setOnClickListener {
            bind.infoCheckbox.apply {
                if(isChecked) bind.allowBtn.setEnabled()
                else bind.allowBtn.setDisabled()
            }
        }
    }

}