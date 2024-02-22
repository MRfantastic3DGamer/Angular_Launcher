package com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun AppLabel (vm: AppLabelVM){
    val position by animateIntOffsetAsState(targetValue = vm.offset.round(), label = "app label")
    if (vm.appName != "-1"){
        Box(modifier = Modifier.fillMaxSize()){
            Box (
                Modifier
                    .offset { position }
                    .fillMaxWidth()
                    .height(ScreenUtils.fToDp(vm.height))
                    .background(Color.Blue),
            ){
                Text(
                    text = vm.appName,
                    maxLines = 1,
                )
            }
        }
    }
}