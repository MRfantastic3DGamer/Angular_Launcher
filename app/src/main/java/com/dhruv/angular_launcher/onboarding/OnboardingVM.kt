package com.dhruv.angular_launcher.onboarding

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.lang.Integer.min
import kotlin.math.max

class OnboardingVM(
    val endOnboarding:()->Unit,
    complete: Boolean
): ViewModel() {

    var currentPage by mutableIntStateOf(0)

    fun checkForPermission (
        context: Context,
        permission: String
    ): Boolean = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    val haveStoragePermission: Boolean
        get() = Environment.isExternalStorageManager()

//    return ContextCompat.checkSelfPermission(
//    context,
//    Manifest.permission.READ_EXTERNAL_STORAGE
//    ) == PackageManager.PERMISSION_GRANTED

    var onBoardingComplete: Boolean by mutableStateOf(complete)

    fun goBack(){
        currentPage = max( 0, currentPage-1 )
    }

    fun goForward(){
        val maxPages = 3
        val gotPermission =
            when (currentPage) {
                0 -> true
                1 -> true
                2 -> true
                else -> false
            }
        println("permission for ${currentPage}: $gotPermission")
        if (gotPermission){
            currentPage = min( maxPages, currentPage+1 )
        }
        if (currentPage == maxPages){
            onBoardingComplete = true
            endOnboarding()
        }
    }

    fun searchIfOnboardingIsComplete(): Boolean{
        if (haveStoragePermission){
            endOnboarding()
            return true
        }
        return false
    }
}