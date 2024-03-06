package com.dhruv.angular_launcher.haptics

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

object HapticsHelper {

    private var fingerSpeed = 0f


    private var glanceOverAppSpeed = 20f
    private val appGlancedHaptic = VibrationEffect.createWaveform(
        longArrayOf(30),
        intArrayOf(70),
        -1
    )
    private val appStoppedHaptic = VibrationEffect.createWaveform(
        longArrayOf(50),
        intArrayOf(160),
        -1
    )
    private val selectedAppHaptic: VibrationEffect
        get() = if (fingerSpeed > glanceOverAppSpeed) appGlancedHaptic else appStoppedHaptic

    fun appSelectHaptic (
        context: Context
    ){
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(selectedAppHaptic)
    }



    private var glanceOverGroupSpeed = 20f
    private val groupGlancedHaptic = VibrationEffect.createWaveform(
        longArrayOf(30),
        intArrayOf(100),
        -1
    )
    private val groupStoppedHaptic = VibrationEffect.createWaveform(
        longArrayOf(50),
        intArrayOf(200),
        -1
    )
    private val selectedGroupHaptic: VibrationEffect
        get() = if (fingerSpeed > glanceOverGroupSpeed) groupGlancedHaptic else groupStoppedHaptic

    fun groupSelectHaptic (
        context: Context
    ){
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(selectedGroupHaptic)
    }

    fun toggleSettingsHaptic (
        context: Context
    ) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
    }

    fun setFingerSpeed (speed: Float) {
        fingerSpeed = speed
//        DebugLayerValues.addString("finger speed: $speed", "fingerSpeed")
//        DebugLayerValues.addString("selected app haptic: ${if (fingerSpeed > glanceOverAppSpeed) "glanced" else "stopped"}", "selected-app-haptic")
//        DebugLayerValues.addString("selected grp haptic: ${if (fingerSpeed > glanceOverAppSpeed) "glanced" else "stopped"}", "selected-grp-haptic")
    }
}
