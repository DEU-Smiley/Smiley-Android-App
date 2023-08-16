package com.example.smiley.hospital

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.hospital.model.Hospital
import com.example.domain.hospital.model.HospitalPositList
import com.example.smiley.R
import com.example.smiley.common.extension.goneWithAnimation
import com.example.smiley.common.extension.resetStatusBarAndNavigationBar
import com.example.smiley.common.extension.setCustomColorStatusBarAndNavigationBar
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.extension.showToast
import com.example.smiley.common.extension.visibleWithAnimation
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.FragmentHospitalMapBinding
import com.example.smiley.hospital.viewmodel.HospitalMapFragmentState
import com.example.smiley.hospital.viewmodel.HospitalMapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HospitalMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HospitalMapFragment : Fragment(), OnMapReadyCallback {

    private var _bind:FragmentHospitalMapBinding? = null
    private val bind get() = _bind!!

    private lateinit var naverMap:NaverMap // 지도 객체
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mapView:MapView by lazy { bind.mapView }
    private val hospitalVm: HospitalMapViewModel by viewModels()
    private val baseMarker = OverlayImage.fromResource(R.drawable.ic_marker)

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_hospital_map, container, false)

        observe()
        initView()

        /** 위치 권한 확인 후 요청 */
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyTouchEffectToAllViews(view as ViewGroup)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    /**
     * 클릭 가능한 모든 뷰에 반투명 효과 적용
     */
    private fun applyTouchEffectToAllViews(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child.isClickable) {
                child.setOnTouchListener(TransparentTouchListener())
            }

            if (child is ViewGroup) {
                applyTouchEffectToAllViews(child)
            }
        }
    }

    private fun initView(){
        showBottomSheet(false)
        bind.customHospitalInfoView.setBottomSheetBehavior(BottomSheetBehavior.from(bind.customHospitalInfoView))
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                hospitalVm.state.collect{ state ->
                    when(state){
                        is HospitalMapFragmentState.Init -> Unit
                        is HospitalMapFragmentState.IsLoading -> handleLoading(true)
                        is HospitalMapFragmentState.SuccessLoadHospitalList -> {
                            handleSuccess(state.hospitalPositList)
                        }
                        is HospitalMapFragmentState.SuccessLoadHospital -> {
                            handleLoading(false)
                            handleLoadHospital(state.hospital)
                        }
                        is HospitalMapFragmentState.Error -> {
                            handleError(state.error)
                        }
                        is HospitalMapFragmentState.ShowToast -> {
                            handleShowToast(state.message)
                        }
                    }
                }
            }
        }
    }


    private fun handleLoading(isLoading:Boolean){
        bind.customHospitalInfoView.setLoading(isLoading)
    }


    private fun handleSuccess(result:HospitalPositList){
        handleLoading(false)
        result.hospitalPosits.forEach {
            addMarker(
                it.lat,
                it.lng,
                it.hpid
            )
        }
    }

    private fun handleLoadHospital(hospital: Hospital){
        handleLoading(false)
        bind.customHospitalInfoView.updateHospitalInfo(hospital)
    }

    private fun handleError(message: String){
        handleLoading(false)
        requireActivity().showConfirmDialog(
            "병원 조회 오류",
            message
        )
    }

    private fun handleShowToast(message:String){
        handleLoading(false)
        requireActivity().showToast(message)
    }

    /**
     * 위치 권한 요청 메소드
     */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)){
            if(!locationSource.isActivated){ // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        setCurrentPostion() // 현재 위치로 이동
        settingMapUi() // Ui Setting

        // 위치 갱신 버튼 클릭시 FusedLocationSource를 통해 현재 위치로 이동
        naverMap.locationSource = locationSource
        naverMap.addOnCameraIdleListener(cameraIdleListener)
        naverMap.setOnMapClickListener { pointF, latLng ->
            showBottomSheet(false)
        }
    }

    /**
     * Naver Map Ui Setting 메소드
     */
    private fun settingMapUi(){
        // 위치 갱신 버튼 표시
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true
    }

    /**
     * 지도를 현재 위치로 옮기는 메소드
     */
    @SuppressLint("MissingPermission")
    private fun setCurrentPostion(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val curLocation = location!!
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(
                        curLocation.latitude,
                        curLocation.longitude
                    )
                )

                naverMap.moveCamera(cameraUpdate)
            }
    }

    /**
     * (lat, lng) 위치에 마커를 생성하는 메소드
     *
     * @param lat Double
     * @param lng Double
     * @param tag String
     */
    private val markerList: ArrayList<Marker> = arrayListOf()
    private fun addMarker(lat:Double, lng:Double, tag: String){
        Marker().apply {
            // 여러 마커가 같은 이미지를 사용하는 경우 OverlayImage는 하나만 만들어놓고 쓰면 됨
            icon = baseMarker
            position = LatLng(lat, lng)
            // 마커를 한 번에 많이 생성하는 경우에는 생성만하고
            // 지도에 추가하는 부분은 별도로 해야할 수도
            this.tag = tag

            map = naverMap
            onClickListener = markerClickListener
        }.also(markerList::add)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

        this.setCustomColorStatusBarAndNavigationBar(Color.TRANSPARENT, Color.WHITE)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()

        this.resetStatusBarAndNavigationBar()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }


    /**
     * 카메라 움직임 종료 이벤트 리스너
     */
    private val cameraIdleListener =
        NaverMap.OnCameraIdleListener {
            markerList.forEach { it.map = null }
            markerList.clear()

            with(naverMap.cameraPosition.target){
                hospitalVm.getNearByHospitals(
                    lat = latitude,
                    lng = longitude,
                    dis = 1.5
                )
                Log.i("NaverMap", "카메라 움직임 종료 중심 좌표(${latitude}, $longitude)")
            }
        }

    private fun showBottomSheet(isShow: Boolean){
        bind.customHospitalInfoView.show(isShow)
        if(isShow){
            bind.clAddBtnLayout.visibleWithAnimation(200)
        } else {
            bind.clAddBtnLayout.goneWithAnimation(200)
        }
    }

    /**
     * 마커 클릭 이벤트 리스너
     */
    private val markerClickListener = Overlay.OnClickListener { overlay ->
        val marker = overlay as Marker
        val hpid = marker.tag as String

        hospitalVm.requestHospital(hpid)

        showBottomSheet(true)

        true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HospitalMapFragment.
         */

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

        @JvmStatic
        fun newInstance() = HospitalMapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}