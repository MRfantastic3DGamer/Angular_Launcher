package com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.core.appIcon.StaticAppIcon
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.utils.ScreenUtils
import kotlin.math.roundToInt

@Composable
fun AppLabel (vm: AppLabelVM) {
    val context = LocalContext.current
    val position by animateIntOffsetAsState(targetValue = vm.offset.round(), label = "app label")
    val DBVM = AppDatabase.getViewModel(context)
    val apps = DBVM.apps.collectAsState(initial = emptyList()).value

    val iconJiggle by animateFloatAsState(
        targetValue = if (vm.iconJiggleTrigger) 1.2f else 1f,
        finishedListener = { vm.iconJiggleTrigger = false },
        animationSpec = if (vm.iconJiggleTrigger) tween(10) else tween(100),
        label = "app label icon jiggle"
    )
    if (iconJiggle == 1.2f) {
        vm.iconJiggleTrigger = false
    }
//    DebugLayerValues.addString("icon jiggle (${vm.iconJiggleTrigger}): $iconJiggle", "icon jiggle")

    if (AppLabelValue.useLaunchTrigger()) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(vm.prevPkg)
        println("got intent for ${vm.prevPkg}: ${launchIntent}")
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        }
        println("launched app ${vm.prevPkg}")
    }
    if (vm.visibility) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .offset { position }
                    .fillMaxWidth()
                    .height(ScreenUtils.fToDp(vm.height)),
            ) {
                Row (
                    Modifier.height((180*1.2).dp),
                    Arrangement.Start,
                    Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    StaticAppIcon(
                        packageName = vm.appPkg,
                        size = ((180 * iconJiggle).roundToInt())
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AppName(apps.firstOrNull { it.packageName == vm.appPkg }?.name)
                }
            }
        }
    }
}