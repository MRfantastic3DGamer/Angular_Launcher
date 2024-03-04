package com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.round
import androidx.core.graphics.drawable.toBitmap
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun AppLabel (vm: AppLabelVM){
    val context = LocalContext.current
    val position by animateIntOffsetAsState(targetValue = vm.offset.round(), label = "app label")

    if (AppLabelValue.useLaunchTrigger()){
        val launchIntent = context.packageManager.getLaunchIntentForPackage(vm.prevPkg)
        println("got intent for ${vm.prevPkg}: ${launchIntent}")
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        }
        println("launched app ${AppLabelValue.GetData.value!!.appPackage}")
    }
    if (vm.visibility){
        Box(modifier = Modifier.fillMaxSize()){
            Box (
                Modifier
                    .offset { position }
                    .fillMaxWidth()
                    .height(ScreenUtils.fToDp(vm.height))
                    .background(Color.Blue),
            ){
                Row {
                    if (vm.appsIcons.containsKey(vm.appPkg)) {
                        Image(
                            bitmap = vm.appsIcons[vm.appPkg]!!.drawable
                                .toBitmap(
                                180,180
                                ).asImageBitmap(),
                            contentDescription = "app ${vm.appPkg}",
                        )
                    }
                    Text(
                        text = vm.appPkg,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}