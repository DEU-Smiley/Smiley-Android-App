package com.example.smiley.hospital

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
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
import com.example.domain.hospital.model.SimpleHospitalList
import com.example.smiley.R
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.extension.showToast
import com.example.smiley.databinding.FragmentHospitalMapBinding
import com.example.smiley.hospital.viewmodel.HospitalMapFragmentState
import com.example.smiley.hospital.viewmodel.HospitalMapViewModel
import com.example.smiley.hospital.viewmodel.HospitalSearchFragmentState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
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

        /** 위치 권한 확인 후 요청 */
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        
        // 현재 위치로 이동
        setCurrentPostion()

        // Ui Setting
        settingMapUi()

        // 위치 갱신 버튼 클릭시 FusedLocationSource를 통해 현재 위치로 이동
        naverMap.locationSource = locationSource
        naverMap.addOnCameraIdleListener(cameraIdleListener)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                hospitalVm.state.collect{ state ->
                    handleStateChange(state)
                }
            }
        }
    }

    private fun handleStateChange(state: HospitalMapFragmentState){
        when(state){
            is HospitalMapFragmentState.Init -> Unit
            is HospitalMapFragmentState.SuccessLoadHospital -> {
                handleSuccess(state.simpleHospitalList)
            }
            is HospitalMapFragmentState.Error -> {
                handleError(state.error)
            }
            is HospitalMapFragmentState.IsLoading -> Unit
            is HospitalMapFragmentState.ShowToast -> {
                handleShowToast(state.message)
            }
        }
    }

    private fun handleSuccess(result:SimpleHospitalList){

    }

    private fun handleError(message: String){
        requireActivity().showConfirmDialog(
            "병원 조회 오류",
            message
        )
    }

    private fun handleLoading(isLoading:Boolean){

    }

    private fun handleShowToast(message:String){
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
     */
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
        }
    }

    /**
     * 카메라 움직임 종료 이벤트 리스너
     */
    private val cameraIdleListener =
        NaverMap.OnCameraIdleListener {

            with(naverMap.cameraPosition.target){
                hospitalVm.getNearByHospitals(
                    lat = latitude,
                    lng = longitude,
                    dis = 1000.0
                )
                Log.i("NaverMap", "카메라 움직임 종료 중심 좌표(${latitude}, $longitude)")
            }
        }

    /**
     * 마커 클릭 이벤트 리스너
     */
    private val markerClickListener = Overlay.OnClickListener { overlay ->
        val marker = overlay as Marker
        val bundle = Bundle()
        bundle.putString("hpid", "${marker.tag}")

        HospitalInfoBottomSheetFragment().apply {
            arguments = bundle
        }.show(parentFragmentManager, HospitalInfoBottomSheetFragment.TAG)

        Log.i("NaverMap", "${marker.tag} 클릭 됨")
        true
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
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
        fun newInstance(param1: String, param2: String) =
            HospitalMapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}