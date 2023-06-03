package com.example.smiley.permission

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.setDisabled
import com.example.smiley.common.extension.setEnabled
import com.example.smiley.databinding.ActivityPermissionBinding
import com.example.smiley.info.InfoActivity

class PermissionActivity : AppCompatActivity() {

    private lateinit var bind:ActivityPermissionBinding
    private val permissionList = App.permissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_permission)
        initAllowBtn()
        addInfoCheckBoxClickEvent()
        addAllowBtnClickEvent()
    }

    private fun initAllowBtn(){
        bind.allowBtn.setDisabled()
    }

    /**
     * 체크 박스 클릭 이벤트
     */
    private fun addInfoCheckBoxClickEvent(){
        bind.infoCheckbox.setOnClickListener {
            bind.infoCheckbox.apply {
                if(isChecked) bind.allowBtn.setEnabled()
                else bind.allowBtn.setDisabled()
            }
        }
    }

    /**
     * 권한 허용하기 버튼 클릭 이벤트
     */
    private fun addAllowBtnClickEvent(){
        bind.allowBtn.setOnClickListener{
            requestPermissions()
        }
    }

    /**
     * 퍼미션 리스트를 받아서 권한을 요청하는 메소드
     */
    private fun requestPermissions() {
        // 허용하지 않은 권한이 있는 경우
        if (!checkPermissions()) {
            // 필요 권한 요청
            ActivityCompat.requestPermissions(this, permissionList, 1)
        }
    }

    /**
     * 필요한 권한이 허용되어 있는지 체크하는 메소드
     */
    private fun checkPermissions():Boolean{
        for (permission in permissionList) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    /**
     * 권한 요청에 대한 사용자 응답 Callback
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 사용자가 권한을 요청하지 않아도 넘어가도록 설정
        // 권한이 필요한 기능에서 별도로 요청하기 때문
        changeActivity(InfoActivity::class.java)
    }
}