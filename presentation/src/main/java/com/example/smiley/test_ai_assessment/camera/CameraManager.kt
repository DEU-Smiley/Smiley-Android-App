package com.example.smiley.test_ai_assessment.camera

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.ScaleGestureDetector
import androidx.camera.core.*
import androidx.camera.core.ImageAnalysis.Analyzer
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val finderView: PreviewView,
    private val graphicOverlay: GraphicOverlay,
    private val analyzer: Analyzer? = null
) {

    companion object {
        private const val TAG = "CameraXBasic"
    }

    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null


    lateinit var cameraExecutor: ExecutorService
    lateinit var imageCapture: ImageCapture
    lateinit var metrics: DisplayMetrics

    private var cameraSelectorOption: Int = CameraSelector.LENS_FACING_BACK
    var rotation: Float = 0f

    init {
        createNewExecutor()
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()

            /**
             * ImageAnalzer로 FaceContourDetectionProcessor가 들어감
             * FaceCountourDetectionProcessor 안에 FaceContourGraphinc 클래스가 Canvas로 그려줌
             * Draw 메소드 안에서 Face 좌표 출력 가능
             * graphicOverlay를 인자로 전달해서 그 위에 그려줌
             */
            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    analyzer?.run {
                        it.setAnalyzer(cameraExecutor,this)
                    }
                }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraSelectorOption)
                .build()
            
            metrics = DisplayMetrics().also { finderView.display.getRealMetrics(it) }
            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
                .build()

            setUpPinchToZoom()
            setCameraConfig(cameraProvider, cameraSelector)
        }, ContextCompat.getMainExecutor(context))
    }

    fun stopCamera(){
        cameraProvider?.unbindAll()
    }

    /**
     * 카메라 구성 설정
     * 카메라를 실제로 바인딩하고 표시하는 부분
     * CameraX는 Lifecycle과 연동 가능
     */
    private fun setCameraConfig(
        cameraProvider: ProcessCameraProvider?,
        cameraSelector: CameraSelector
    ) {
        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )
            preview?.setSurfaceProvider(
                finderView.surfaceProvider
            )
        } catch (e: Exception) {
            Log.e(TAG, "Use case binding failed", e)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPinchToZoom() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio: Float = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F
                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }
        val scaleGestureDetector = ScaleGestureDetector(context, listener)
        finderView.setOnTouchListener { _, event ->
            finderView.post {
                scaleGestureDetector.onTouchEvent(event)
            }
            return@setOnTouchListener true
        }
    }

    fun changeCameraSelector() {
        cameraProvider?.unbindAll()
        cameraSelectorOption =
            if (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        graphicOverlay.toggleSelector()

        startCamera()
    }

    fun isHorizontalMode() : Boolean {
        return rotation == 90f || rotation == 270f
    }

    fun isFrontMode() : Boolean {
        return cameraSelectorOption == CameraSelector.LENS_FACING_FRONT
    }
}