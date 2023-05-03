package com.example.smiley.bluetooth.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val btAdapter: BluetoothAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = context
            .getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        bluetoothManager.adapter
    }
    private val btScanner: BluetoothLeScanner = btAdapter.bluetoothLeScanner
    private var btGatt:BluetoothGatt? = null

    private var isScanning = false
    private val deviceList = mutableSetOf<BluetoothDevice>() // 스캔된 장치 리스트

    private val _state = MutableStateFlow<BluetoothSearchFragmentState>(BluetoothSearchFragmentState.Init)
    val state: StateFlow<BluetoothSearchFragmentState>
        get() = _state

    /**
     * BLE 스캔 시작 메소드
     */
    fun startScan() {
        // 이 부분은 View에서 해도될 듯
        if (btAdapter.isEnabled) {
            // 블루투스가 꺼져있는 경우
            // 블루투스 활성화 요청 코드 or State 체인지
        }

        if (context.checkSelfPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            // 위치 권한 요청 필요 (View에서 해야함)
            _state.value = BluetoothSearchFragmentState.RequiredPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            return
        }
        
        Log.i(TAG, "스캔 시작")
        /**
         * !! Handler로 SCAN_PERIOD 시간 이후엔 종료되도록 하기 !!
         */
        Handler().postDelayed({
            stopScan()
        }, SCAN_PERIOD)

        isScanning = true
        deviceList.clear() // 스캔 시작하면 기존 리스트 초기화
        _state.value = BluetoothSearchFragmentState.IsScanning(UUID.randomUUID(), deviceList)
        btScanner.startScan(scanCallback)
    }

    /**
     * BLE 스캔 종료 메소드
     */
    @SuppressLint("MissingPermission")
    fun stopScan(){
        isScanning = false
        // 스캔을 취소하면 초기 상태로 돌림
        _state.value = BluetoothSearchFragmentState.Init
        btScanner.stopScan(scanCallback)
    }

    /**
     * 장치 연결 메소드
     */
    fun connectToDevice(address:String){
        val device = btAdapter.getRemoteDevice(address)
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            /**
             * 블루투스 연결 권한 요청 코드 작성
             */
            return
        }
        
        // SDK 버전에 따라 처리해줘야할 수도 있음
        // Ref : https://doqtqu.tistory.com/174
        btGatt = device.connectGatt(
            context,
            false,
            connectedCallback,
            BluetoothDevice.TRANSPORT_LE
        )
    }

    /**
     * 장치 연결 해제 메소드
     */
    @SuppressLint("MissingPermission")
    fun disConnectToDevice(){
        btGatt?.let {
            it.disconnect()
            it.close()
        }
        btGatt = null
    }

    @SuppressLint("MissingPermission")
    private val scanCallback = object : ScanCallback() {
        /**
         * BLE 장치가 검색되면 호출
         */
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d(TAG, "ScanResult = ${result?.device?.name} 찾음")

            // 기기의 이름이 null이 아니고, 추가되지 않은 기기인 경우에만 리스트에 추가
            result?.let {
                if (!deviceList.contains(result.device) && result.device.name != null) {
                    deviceList.add(result.device)
                }
                // notify
                _state.value = BluetoothSearchFragmentState.IsScanning(
                    id = UUID.randomUUID(),
                    scanResults = deviceList
                )
            }
        }

        /**
         * Batch Result가 전달되면 호출
         * Batch Result는 이전 검색 결과와 새로운 검색 결과를 합친 결과 리스트
         */
        @SuppressLint("MissingPermission")
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d(TAG, "BatchScanResult = $results")
            /**
             * 기기의 이름이 null이 아니고, 추가되지 않은 기기인 경우에만 리스트에 추가
             */
            results?.let {
                it.forEach { result ->
                    if(!deviceList.contains(result.device) && result.device.name != null){
                        deviceList.add(result.device)
                    }
                }
            }
        }

        /**
         * 스캔에 실패했을 때 호출되는 메소드
         */
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(TAG, "ScanFailed(code = $errorCode")
            // State를 ScanFailed로 변환
            _state.value = BluetoothSearchFragmentState.Error(
                "장치 연결에 실패하였습니다. (errorCode: $errorCode)"
            )
        }
    }

    private val connectedCallback = object : BluetoothGattCallback() {
        /**
         * BLE Connection 상태가 변경될 때 호출
         */
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            btGatt = gatt
            when(newState){
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.i(TAG, "GATT 서버에 연결 되었습니다.")
                    btGatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.i(TAG, "GATT 서버로 부터 연결을 해제합니다.")
                    disConnectToDevice()
                }
            }
        }

        /**
         * 새로운 장치가 발견된 경우
         * 장치에 대한 원격 서비스, 특성 및 설명자 목록이 업데이트 되었을 때 호출
         */
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            // 여기서 device 리스트에 추가해야할 듯?
            // 추가될 때마다 BluetoothFragment 리스트에 표시?
            when(status){
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "연결에 성공했습니다.")
                    // 연결 성공 State
                    gatt?.let {
                        btGatt = it
                        _state.value = BluetoothSearchFragmentState.SuccessConnect(it.device)
                    }
                }
                else -> {
                    Log.w(TAG, "연결에 실패했습니다. status : $status")
                    // 연결 실패 state
                    _state.value = BluetoothSearchFragmentState.Error("장치 연결에 실패했습니다. (status: $status)")
                }
            }
        }
    }

    companion object {
        private const val TAG = "BluetoothViewModel"
        private const val SCAN_PERIOD = 10000L
    }
}

sealed class BluetoothSearchFragmentState {
    object Init             : BluetoothSearchFragmentState()
    data class Error(val message:String)           : BluetoothSearchFragmentState()
    data class SuccessConnect(val device: BluetoothDevice)  : BluetoothSearchFragmentState()
    data class FailedScan(val message: String)      : BluetoothSearchFragmentState()
    data class RequiredPermission(val permission:String) : BluetoothSearchFragmentState()
    data class IsConnecting(val status:Boolean)    : BluetoothSearchFragmentState()
    data class IsScanning(
        private val id: UUID,
        val scanResults: Set<BluetoothDevice>
    ) : BluetoothSearchFragmentState()
}