package com.dhruv.angular_launcher.accessible_screen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.data.ScreenValues
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun AccessibleScreenPresentation(vm: AccessibleScreenVM){

    val context = LocalContext.current
    val screenData = ScreenValues.GetData.value
    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(text = vm.screenData.sliderTopPosition.toString())
            Text(text = vm.screenData.navigationMode .name)
            Text(text = vm.screenData.selectionMode  .name)
            Text(text = vm.screenData.navigationStage.name)
        }
        Box(
            modifier = Modifier
                .size(
                    width = vm.persistentData.sliderWidth,
                    height = vm.persistentData.sliderHeight
                )
                .offset(ScreenUtils.fromRight(vm.persistentData.sliderWidth) , ScreenUtils.fToDp(screenData?.sliderTopPosition ?: 0f))
                .background(Color.Red)
        )
    }
}