package com.vecentek.decorelinkdemo

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import com.vecentek.decorelink.Constants
import com.vecentek.decorelink.DLConfigure
import com.vecentek.decorelink.DLEngine
import com.vecentek.decorelink.base.bean.ScanDevice
import com.vecentek.decorelink.base.util.LogUtil
import com.vecentek.decorelink.base.util.TimeUtils
import com.vecentek.decorelink.ble.interfaces.BleScanResult
import com.vecentek.decorelink.sdk.bean.keys.DLICCEKey
import com.vecentek.decorelink.sdk.callback.ApiPublicCallBack
import com.vecentek.decorelink.sdk.callback.info.CarRemindCallBackInfo
import com.vecentek.decorelink.sdk.callback.info.DLResult
import com.vecentek.decorelink.sdk.callback.info.VehicleStateCallBackInfo
import java.nio.ByteBuffer

@SuppressLint("MissingPermission")
class DLMain private constructor(){

    var phone = ""
    var vin = ""
    var url = ""

    //钥匙
    private var key:DLICCEKey? = null

    //单例模式
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DLMain()
        }
    }

    lateinit var log:LogResult

    interface LogResult{
        fun onLog(info:String)
        fun onKey(key:DLICCEKey)
        fun onSelect(keyId: String)
    }

    fun observeLog(result:LogResult){
        log = result
    }

    fun initSDK(){
        log.onLog("初始化SDK开始")
        val config = DLConfigure.Builder()
            .setApplication(MyApplication.getInstance().applicationContext)//设置application引用
            .setDeviceFingerPrint(deviceFinger)//设置设备指纹16位
            .setURL(url) //后台服务器url
            .build()
        DLEngine.instance.configureSDK(config)
        log.onLog("初始化SDK完成")
    }

    fun setCurrentUser(){
        DLEngine.instance.setCurrentUser(phone)
        log.onLog("设置账号$phone")
    }

    fun observeBleStatus(){
        log.onLog("监听开始")
        DLEngine.instance.bleVersionNegotiationPass {
            log.onLog("版本协商")
        }
        DLEngine.instance.bleManagerState {
            LogUtil.log("蓝牙状态：${it.statusName}")
            log.onLog("蓝牙状态：$it")
        }
        DLEngine.instance.bleBondState {
            LogUtil.log("蓝牙绑定状态：${it.statusName}")
            log.onLog("蓝牙绑定状态：$it")
        }
        DLEngine.instance.setPublicCallBack(object : ApiPublicCallBack {
            override fun bleRemindMsg(carRemind: CarRemindCallBackInfo) {
                LogUtil.log("bleVehicleState -> ${carRemind.message}")
                log.onLog("bleVehicleState -> ${carRemind.message}")
            }

            override fun bleVehicleControlStateNotify(result: DLResult) {
                logDL(result, "bleVehicleControlStateNotify")
            }

            override fun bleVehicleState(result: VehicleStateCallBackInfo) {
                LogUtil.log("bleVehicleState -> ${result.message}")
                log.onLog("bleVehicleState -> ${result.message}")
            }

            override fun whiteListOffLineBreathAvailableTimes(result: DLResult) {
                logDL(result, "whiteListOffLineBreathAvailableTimes")
            }
        })
        log.onLog("监听完成")
    }

    fun bleScanAndConnect(){
        DLEngine.instance.startBleScan(object : BleScanResult {
            override fun onScanResult(r: ScanDevice) {
                if (r.vin.isNotEmpty() && vin.contains(r.vin,true)) {
                    stopBleScan()
                    connectBle(r) {
                        LogUtil.log("蓝牙连接回调")
                    }
                }
            }

            override fun onError(p0: String?) {}
        })
    }

    fun bleScan(){
            log.onLog("开始扫描")
            var count = 0
            DLEngine.instance.startBleScan(object : BleScanResult {
                override fun onScanResult(r: ScanDevice) {
                    count++
                    if (count>7){
                        stopBleScan()
                    }
                }

                override fun onError(p0: String?) {
                    log.onLog("扫描报错：$p0")
                }
            })
    }

    fun stopBleScan(){
        log.onLog("停止扫描")
        DLEngine.instance.stopBleScan()
    }

    fun disConnectBle(){
        DLEngine.instance.disConnectBle()
    }

    fun connectBle(device: ScanDevice , callback: (ScanDevice) -> Unit){
        DLEngine.instance.connectBleDevice(device) {
            callback(it)
        }
    }

    fun syncKeys(){
        DLEngine.instance.syncKeys {
            logDL(it, "syncKeys")
            if (it.flag == Constants.SUCCESS) {
                DLICCEKey.parseKeys(it.data).apply {
                    if (size > 0) {
                        this.forEach { key1 ->
                            log.onLog("钥匙：${key1.keyID}，${key1.vin}")
                            log.onKey(key1)
                        }
                        key = this[0]
                    }
                }
            }
        }
    }

    fun openKey(){
        DLEngine.instance.openKey(vin) {
            logDL(it, "openKey")
        }
    }
    fun selectKey(keyId:String? = null){
        keyId?.let { keyIdStr ->
            DLEngine.instance.selectKey(keyIdStr) {
                logDL(it, "selectKey:$keyIdStr")
                log.onSelect(keyIdStr)
            }
        }?:run {
            key?.keyID?.let { newKeyId ->
                DLEngine.instance.selectKey(newKeyId) {
                    logDL(it, "selectKey:$newKeyId")
                    log.onSelect(newKeyId)
                }
            } ?: run {
                LogUtil.log("没有钥匙")
            }
        }
    }

    fun activationKey(){
        DLEngine.instance.activationKey {
            logDL(it, "activationKey")
        }
    }

    fun revokeKey(){
        key?.keyID?.let { keyId ->
            DLEngine.instance.revokeKey(keyId) {
                logDL(it, "revokeKey")
            }
        }
    }

    fun shareKey(){
        key?.let { keyInfo ->
            val array = byteArrayOf(0x01, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01)
            DLEngine.instance.shareKey(
                ByteBuffer.wrap(array).int,
                TimeUtils.getTime(System.currentTimeMillis(), "yyyyMMdd'T'HHmmss'Z'"),
                TimeUtils.getTime(
                    System.currentTimeMillis() + 60000L,
                    "yyyyMMdd'T'HHmmss'Z'"
                ),
                "18000000001",
                keyInfo.vin,
                keyInfo.keyID,
                "1234567890123456"
            ) {
                logDL(it, "shareKey")
            }
        }
    }

    fun vehicleControl(){
        DLEngine.instance.vehicleControl(1, 0) {
            logDL(it, "deleteKey")
        }
    }

    fun personalizedSettings(){
        key?.let { keyInfo ->
            DLEngine.instance.personalInfoSet(1, keyInfo.keyID) {
                logDL(it, "personalInfoSet")
            }
        }
    }

    fun calibrationDataIsExist(){
        key?.let { keyInfo ->
            DLEngine.instance.calibrationDataIsExist(keyInfo.vin) {
                logDL(it, "calibrationDataIsExist")
            }
        }
    }

    fun syncKeyOtherPlatform(){
        DLEngine.instance.syncKeyOtherPlatform {
            logDL(it, "syncKeyOtherPlatform")
        }
    }

    fun setSelfCalibrationLevel(){
        DLEngine.instance.setSelfCalibrationLevel(5) {
            logDL(it, "setSelfCalibrationLevel")
        }
    }

    fun logDL(
        dlResult: DLResult,
        info: String = "",
    ) {
        LogUtil.log("$info => flag=${dlResult.flag}, message=${dlResult.message}, data=${dlResult.data}, code=${dlResult.code}, data=${dlResult.data}")
        log.onLog("$info => ${if (dlResult.flag == Constants.SUCCESS) "成功" else "失败"}${if (dlResult.flag == Constants.SUCCESS) "" else ", ${dlResult.message}" }")

    }
}