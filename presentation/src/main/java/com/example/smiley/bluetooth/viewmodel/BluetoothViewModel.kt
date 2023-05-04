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
import com.example.smiley.bluetooth.util.BluetoothUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    private fun setStateToInit(){
        _state.value = BluetoothSearchFragmentState.Init
    }
    private fun setStateToError(message:String){
        _state.value = BluetoothSearchFragmentState.Error(message)
    }

    private fun setStateToSuccessConnect(device: BluetoothDevice){
        _state.value = BluetoothSearchFragmentState.SuccessConnect(device)
    }

    private fun setStateToFailedScan(message: String){
        _state.value = BluetoothSearchFragmentState.FailedScan(message)
    }

    private fun setStateToRequiredPermission(permission: String){
        _state.value = BluetoothSearchFragmentState.RequiredPermission(permission)
    }

    private fun setStateToIsConnecting(status: Boolean){
        _state.value = BluetoothSearchFragmentState.IsConnecting(status)
    }

    private fun setStateToIsScanning(id:UUID, scanResults: Set<BluetoothDevice>){
        _state.value = BluetoothSearchFragmentState.IsScanning(id, scanResults)
    }

    /**
     * BLE 스캔 시작 메소드
     */
    fun startScan() {
        // 이 부분은 View에서 해도될 듯
        if (btAdapter.isEnabled) {
            // 블루투스가 꺼져있는 경우
            // 블루투스 활성화 요청 코드 or State 체인지
        }

        if (context.checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // 위치 권한 요청 필요 (View에서 해야함)
            setStateToRequiredPermission(ACCESS_FINE_LOCATION)
            return
        }
        
        // SCAN_PERIOD 시간 이후엔 검색 종료
        Handler().postDelayed({
            stopScan()
        }, SCAN_PERIOD)

        isScanning = true
        deviceList.clear() // 스캔 시작하면 기존 리스트 초기화
        setStateToIsScanning(UUID.randomUUID(), deviceList)
        btScanner.startScan(scanCallback)
    }

    /**
     * BLE 스캔 종료 메소드
     */
    @SuppressLint("MissingPermission")
    fun stopScan(){
        isScanning = false
        // 스캔을 취소하면 초기 상태로 돌림
        setStateToInit()
        btScanner.stopScan(scanCallback)
    }

    /**
     * 장치 연결 메소드
     */
    fun connectToDevice(address:String){
        val device = btAdapter.getRemoteDevice(address)
        if (ActivityCompat.checkSelfPermission(context,BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED) {
            /**
             * 블루투스 연결 권한 요청 코드 작성
             */
            return
        }
        
        // SDK 버전에 따라 처리해줘야할 수도 있음
        // Ref : https://doqtqu.tistory.com/174
        setStateToIsConnecting(true)
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
        setStateToError("장치 연결에 실패했습니다.")
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
                setStateToIsScanning(
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
            // 기기의 이름이 null이 아니고, 추가되지 않은 기기인 경우에만 리스트에 추가
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

            setStateToError("장치 연결에 실패하였습니다. (errorCode: $errorCode)")
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
        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            setStateToIsConnecting(false)
            when(status){
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.i(TAG, "연결에 성공했습니다.")
                    gatt?.let {
                        btGatt = it
                        // 값을 읽기 위해서 ResponseCharacteristic 받아오기
                        val respCharacteristic = BluetoothUtil.findResponseCharacteristic(it)
                        if(respCharacteristic == null){
                            setStateToError("장치의 특성(Characteristic)을 찾을 수 없습니다.")
                            disConnectToDevice()
                            return
                        }
                        it.setCharacteristicNotification(respCharacteristic, true)

                        val descriptor:BluetoothGattDescriptor = respCharacteristic.getDescriptor(
                            UUID.fromString(BluetoothUtil.CLIENT_CHARACTERISTIC_CONFIG)
                        )
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(descriptor)
                        // 연결 성공 State로 변경
                        setStateToSuccessConnect(it.device)
                    }
                }
                else -> {
                    // 연결 실패 state
                    setStateToError("장치 연결에 실패했습니다. (status: $status)")
                }
            }
        }

        /**
         * BLE 장치의 특성(Characteristic)이 변경되면 호출됨
         * BLE 장치에서 전송하는 데이터를 해당 메소드에서 읽을 수 있음
         */
        @SuppressLint("MissingPermission")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)

            Log.d("캐릭터 밸류", characteristic.getStringValue(0))
        }
    }

    companion object {
        private const val TAG = "BluetoothViewModel"
        private const val SCAN_PERIOD = 10000L
        private const val ACCESS_FINE_LOCATION  = android.Manifest.permission.ACCESS_FINE_LOCATION
        private const val BLUETOOTH_CONNECT     = android.Manifest.permission.BLUETOOTH_CONNECT
    }
}

sealed class BluetoothSearchFragmentState {
    object Init                                             : BluetoothSearchFragmentState()
    data class Error(val message:String)                    : BluetoothSearchFragmentState()
    data class SuccessConnect(val device: BluetoothDevice)  : BluetoothSearchFragmentState()
    data class FailedScan(val message: String)              : BluetoothSearchFragmentState()
    data class RequiredPermission(val permission:String)    : BluetoothSearchFragmentState()
    data class IsConnecting(val status:Boolean)             : BluetoothSearchFragmentState()
    data class IsScanning(
        private val id: UUID,
        val scanResults: Set<BluetoothDevice>
    ) : BluetoothSearchFragmentState()
}