package com.dhruv.angular_launcher.permission_handler

data class PermissionModel(
    val permission: String,
    val maxSDKVersion: Int,
    val minSDKVersion: Int,
    val rational: String,
)