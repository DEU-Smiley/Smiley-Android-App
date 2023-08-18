package com.example.smiley.login

import android.content.Intent
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.repeatOnStarted
import com.example.smiley.common.extension.setTransparentStatusBarAndNavigationBar
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.databinding.ActivityLoginBinding
import com.example.smiley.info.InfoActivity
import com.example.smiley.login.viewmodel.LoginActivityState
import com.example.smiley.login.viewmodel.LoginViewModel
import com.example.smiley.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var bind: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initLoginVideoView()
        observeLoginState()
        addLoginBtnClickEvent()
    }

    private fun initLoginVideoView(){
        bind.ttvLoginVideo.surfaceTextureListener = surfaceTextureListener
        this.setTransparentStatusBarAndNavigationBar()
    }

    /**
     * 카카오 로그인 요청 메소드
     */
    private fun addLoginBtnClickEvent() {
        bind.clKakaoLoginBtn.setOnClickListener {
            viewModel.kakaoLogin(this)
        }
    }

    private fun observeLoginState() {
        repeatOnStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is LoginActivityState.Init -> Unit
                    is LoginActivityState.SuccessLogin -> handleSuccessLogin()
                    is LoginActivityState.RequiredSignUp -> handleRequiredSignUp(state.userId)
                    is LoginActivityState.Error -> handleErrorLogin(state.message)
                }
            }
        }
    }

    private fun handleSuccessLogin() {
        // 권한 요청은 로그인 전이나 홈 화면에서에 팝업이나 바텀시트 형태로 변경
        changeActivity(MainActivity::class.java)
    }

    private fun handleRequiredSignUp(userId: String){
        val intent = Intent(this, InfoActivity::class.java).apply {
            putExtra("userId", userId)
        }

        startActivity(intent)
        finish()
    }

    private fun handleErrorLogin(message: String) {
        showConfirmDialog("로그인 에러", content = message)
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer?.release()
        mediaPlayer = null
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            val video: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_login)

            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@LoginActivity, video)
                setSurface(Surface(surface))
                prepareAsync()
                isLooping = true

                setOnPreparedListener {
                    this.start()
                }
            }
        }
        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) = Unit
        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean = false
        override fun onSurfaceTextureUpdated(p0: SurfaceTexture) = Unit
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}