package com.example.smiley.selfassessment.fragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.OrientationEventListener
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smiley.R
import com.example.smiley.common.base.BaseFragment
import com.example.smiley.common.extension.addFragmentToFullScreen
import com.example.smiley.databinding.FragmentCameraBinding
import com.example.smiley.selfassessment.fragment.AssessmentResultFragment.Companion.ASSESSMENT_TYPE
import com.example.smiley.test_ai_assessment.camera.CameraManager
import com.example.smiley.test_ai_assessment.mlkit.FaceContourDetectionProcessor
import com.example.smiley.test_ai_assessment.util.BitmapSavedCallback
import com.example.smiley.test_ai_assessment.util.drawBitmapOnCanvas
import com.example.smiley.test_ai_assessment.util.imageToBitmap
import com.example.smiley.test_ai_assessment.util.rotateFlipImage
import com.example.smiley.test_ai_assessment.util.saveToGallery
import com.example.smiley.test_ai_assessment.util.scaleImage
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraFragment : BaseFragment<FragmentCameraBinding>(R.layout.fragment_camera) {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10

        @JvmStatic
        fun newInstance(type: AssessmentResultFragment.AssessmentType) = CameraFragment().apply {
            arguments = Bundle().apply {
                putString(ASSESSMENT_TYPE, type.name)
            }
        }
    }

    private var assessmentType: AssessmentResultFragment.AssessmentType? = null
    private lateinit var cameraManager: CameraManager

    private val REQUIRED_CAMERA_PERMISSIONS: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU == Android 13
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            assessmentType = it.getString(ASSESSMENT_TYPE)?.let { name ->
                AssessmentResultFragment.AssessmentType.valueOf(name)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        cameraManager.stopCamera()
    }

    override fun onResume() {
        super.onResume()

        checkPermission()
    }

    override fun initView() {
        createCameraManager()

 //       checkPermission()
        addBtnClickEvent()
    }

    private fun checkPermission(){
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_CAMERA_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // 퍼미션 얻는 메소드
    private fun allPermissionsGranted() = REQUIRED_CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    // 카메라 매니저 생성 메소드
    private fun createCameraManager() {
        cameraManager = CameraManager(
            context = requireContext(),
            lifecycleOwner = this,
            finderView = bind.previewViewFinder,
            graphicOverlay = bind.graphicOverlayFinder,
            analyzer = FaceContourDetectionProcessor(
                bind.graphicOverlayFinder,
                faceDetectResultCallback
            )
        )
    }

    private fun addBtnClickEvent() {
        // 카메라 전환 버튼 이벤트
        bind.cameraChangeBtn.setOnClickListener{
            cameraManager.changeCameraSelector()
        }

        // 촬영 버튼 클릭 이벤트
        bind.ibCameraBtn.setOnClickListener {
            takePicture()
        }
    }

    /**
     * 사진 촬영 메소드
     */
    private fun takePicture() {
        Toast.makeText(requireContext(), "take a picture!", Toast.LENGTH_SHORT).show()

        setOrientationEvent()
        with(cameraManager){
            imageCapture.takePicture(
                cameraExecutor, onImageCaptureCallback
            )
        }
    }

    /**
     * 디바이스의 방향이 변경될 때 카메라의 회전 각도를 설정하는 메소드
     * 방향에 다라 카메라의 회전 각도를 설정
     */
    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Float = when (orientation) {
                    in 45..134 -> 270f
                    in 135..224 -> 180f
                    in 225..314 -> 90f
                    else -> 0f
                }
                cameraManager.rotation = rotation
            }
        }
        orientationEventListener.enable()
    }

    /**
     * 이미지를 비트맵으로 갤러리에 저장하는 메소드
     * @param image Image
     */
    private fun saveImageToGallery(image: Image) {
        val appContext = context ?: return

        image.imageToBitmap()
            ?.rotateFlipImage(cameraManager.rotation, cameraManager.isFrontMode())
            ?.scaleImage(bind.previewViewFinder, cameraManager.isHorizontalMode())
            ?.let { bitmap ->
                val graphicOverlay = bind.graphicOverlayFinder
                val processedBitmap = bind.graphicOverlayFinder.processBitmap

                bitmap.drawBitmapOnCanvas(graphicOverlay, cameraManager.isHorizontalMode())
                processedBitmap.saveToGallery(appContext, onBitmapSavedCallback)
            }
    }

    // FaceContourDetectionProcessor의 처리 결과를 받아오기 위함
    private val faceDetectResultCallback: (face: Face) -> Unit = {
        // TODO 로그 찍어서 Face 값이 여러 개가 오진 않는지 확인
    }

    private val onBitmapSavedCallback = object : BitmapSavedCallback {
        override fun onSaved(path: String) {
            assessmentType ?: return

            this@CameraFragment.addFragmentToFullScreen(
                AssessmentResultFragment.newInstance(
                    imagePath = path,
                    type = assessmentType!!
                )
            )
        }
    }

    private val onImageCaptureCallback = object : ImageCapture.OnImageCapturedCallback() {
        @SuppressLint("UnsafeExperimentalUsageError", "RestrictedApi")
        @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
        override fun onCaptureSuccess(image: ImageProxy) {
            image.image?.let {
                saveImageToGallery(it)
            }

            super.onCaptureSuccess(image)
        }
    }
}