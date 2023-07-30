package com.example.smiley.login

import android.graphics.Color
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.dataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.smiley.R
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.setTransparentStatusBarAndNavigationBar
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.extension.showGenericAlertDialog
import com.example.smiley.databinding.ActivityLoginBinding
import com.example.smiley.login.viewmodel.LoginActivityState
import com.example.smiley.login.viewmodel.LoginViewModel
import com.example.smiley.main.MainActivity
import com.example.smiley.permission.PermissionActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.IOException

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var bind: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // 카카오 자동 로그인 체크
        initLoginVideoView()
        checkKakaoAccessToken()
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
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is LoginActivityState.Init -> Unit
                    is LoginActivityState.SuccessLogin ->
                        handleSuccessLogin(state.user, state.isFirstAccess)
                    is LoginActivityState.ErrorLogin -> handleErrorLogin(state.message)
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun handleSuccessLogin(user: User, accessFlag:Boolean) {
        /**
         * 가입된 회원인 지 체크 필요
         * 가입된 경우 -> 메인으로 바로 이동
         * 미가입인 경우 -> 회원 가입 페이지로 이동
         */
        changeActivity(
            // 최초 접속인 경우
            if(accessFlag) PermissionActivity::class.java
            else MainActivity::class.java
        )
    }

    private fun handleErrorLogin(message: String) {
        showConfirmDialog("로그인 에러", content = message)
    }

    /**
     * 카카오 토큰이 있는지 검증하는 메소드
     */
    private fun checkKakaoAccessToken(){
        // 토큰이 이미 있는 경우
        if(AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo{ _, error ->
                if(error == null) { // 토큰 유효성 체크 성공 (필요시 토큰 갱신)
                    changeActivity(MainActivity::class.java)

                    return@accessTokenInfo
                } else if (error !is KakaoSdkError || !error.isInvalidTokenError()){
                    this.showGenericAlertDialog("사용자 검증에 실패하였습니다. 로그인을 다시 진행해주세요.")
                }
            }
        }
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