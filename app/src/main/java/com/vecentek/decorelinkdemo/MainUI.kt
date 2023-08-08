package com.vecentek.decorelinkdemo

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vecentek.decorelink.sdk.bean.keys.DLICCEKey


@Composable
@SuppressLint("MissingPermission")
fun Content() {
    val logList = remember { mutableStateListOf<String>() }
    val keyList = remember { mutableStateListOf<DLICCEKey>() }
    var selectKey by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("") }
    DLMain.instance.observeLog(object : DLMain.LogResult {
        override fun onLog(info: String) {
            if (logList.size>30) {
                logList.clear()
            }
            logList.add("$info【${Thread.currentThread().name}】")
        }

        override fun onKey(key: DLICCEKey) {
            keyList.clear()
            keyList.add(key)
        }

        override fun onSelect(keyId: String) {
            selectKey = keyId
        }


    })
    Column {
        Column {
            Row {
                DropDownUrl{
                    url = it
                }
                Text(text = url, modifier = Modifier.align(Alignment.CenterVertically))
            }
            Row {
                DropDownUser{
                    user = it
                }
                Text(text = user, modifier = Modifier.align(Alignment.CenterVertically))
            }
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(keyList) {
                    Row(
                        Modifier
                            .padding(5.dp)
                            .background(color = Color.Yellow, shape = RoundedCornerShape(5.dp))
                            .clickable { DLMain.instance.selectKey(it.keyID) }
                            .padding(10.dp)
                    ) {
                        if (selectKey == it.keyID){
                            Text(text = "已选择   ", color = Color.Green)
                        }
                        Text(text = it.vin)
                        if (it.isActivation){
                            Text(text = "   已激活", color = Color.Blue)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    MyButton("初始化") {
                        DLMain.instance.initSDK()
                    }
                    MyButton("设置用户") {
                        DLMain.instance.setCurrentUser()
                    }
                    MyButton("监听蓝牙状态") {
                        DLMain.instance.observeBleStatus()
                    }
                    MyButton("扫描连接蓝牙") {
                        DLMain.instance.bleScanAndConnect()
                    }
                    MyButton("扫描") {
                        DLMain.instance.bleScan()
                    }
                    MyButton("停止扫描") {
                        DLMain.instance.stopBleScan()
                    }
                    MyButton("断开蓝牙") {
                        DLMain.instance.disConnectBle()
                    }
                    MyButton("同步钥匙") {
                        DLMain.instance.syncKeys()
                    }
                    MyButton("开通钥匙") {
                        DLMain.instance.openKey()
                    }
                    MyButton("选择钥匙") {
                        DLMain.instance.selectKey()
                    }
                    MyButton("激活钥匙") {
                        DLMain.instance.activationKey()
                    }
                    MyButton("吊销钥匙") {
                        DLMain.instance.revokeKey()
                    }
                    MyButton("分享钥匙") {
                        DLMain.instance.shareKey()
                    }
                    MyButton("控车") {
                        DLMain.instance.vehicleControl()
                    }
                    MyButton("个性化设置") {
                        DLMain.instance.personalizedSettings()
                    }
                    MyButton("后台是否存在预标定数据") {
                        DLMain.instance.calibrationDataIsExist()
                    }
                    MyButton(msg = "同步小程序钥匙") {
                        DLMain.instance.syncKeyOtherPlatform()
                    }
                    MyButton(msg = "自标定等级") {
                        DLMain.instance.setSelfCalibrationLevel()
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(1f),
            ) {
                items(logList) {
                    Text(text = it)
                }
            }
        }
    }
}

