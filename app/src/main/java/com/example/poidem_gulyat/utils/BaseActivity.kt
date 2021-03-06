package com.example.poidem_gulyat.utils

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

private const val PERMISSION_REQUEST_CODE: Int = 10_000

abstract class BaseActivity: AppCompatActivity() {
    private val permissions: List<String> = arrayListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !PermissionTool.isPermissionsSetGranted(this, permissions)
        ) {
            ActivityCompat.requestPermissions(this,
                PermissionTool.getNotGranted(this, permissions).toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    fun allPermissionGrandted():Boolean = PermissionTool.isPermissionsSetGranted(this, permissions)

        abstract fun onAfterRequestPermission()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }else onAfterRequestPermission()
    }
}