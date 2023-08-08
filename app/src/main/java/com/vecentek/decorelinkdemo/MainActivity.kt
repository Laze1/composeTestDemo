package com.vecentek.decorelinkdemo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
        initPermissions()
    }

    private fun initPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }

        XXPermissions.with(this)
            // 申请单个权限
            // .permission(Permission.RECORD_AUDIO)
            // 申请多个权限
            .permission(permissions)
            // 设置权限请求拦截器（局部设置）
            //.interceptor(new PermissionInterceptor())
            // 设置不触发错误检测机制（局部设置）
            .unchecked()
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        Toast.makeText(
                            this@MainActivity,
                            "获取部分权限成功，但部分权限未正常授予",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return
                    }

                    Toast.makeText(this@MainActivity, "获取权限成功", Toast.LENGTH_SHORT).show()
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        Toast.makeText(
                            this@MainActivity,
                            "被永久拒绝授权，请手动授予权限",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@MainActivity, permissions)
                    } else {
                        Toast.makeText(this@MainActivity, "获取权限失败", Toast.LENGTH_SHORT).show()
                    }
                }
            })

    }
}


