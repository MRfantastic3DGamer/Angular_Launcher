package com.dhruv.angular_launcher.core.resources

import android.content.Context
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.media.AudioManager
import android.os.BatteryManager
import java.util.Date


enum class AllResources{

    // from nested app routs
    BackBuffer,                         // not implemented
    Frame,                              // implemented
    TouchPosition,                      // implemented
    TouchStartPositions,                // implemented
    IconsPositions,                     // implemented
    IconsCount,                         // implemented
    SelectedIconIndex,                  // implemented
    GroupsPositioning,                  // implemented
    GroupsCount,                        // implemented
    SelectedGroupIndex,                 // implemented
    GroupZoneStartRadios,               // implemented
    GroupZoneEndRadios,                 // implemented
    LabelYLimits,                       // implemented
    Resolution,                         // implemented

    // from functions
    Battery,                            // implemented
    DateTime,                           // implemented
    DarkMode,                           // implemented
    Volume,                             // implemented
    Charging,                           // implemented

    // from sensors
    Accelerometer,                //
    MagneticField,                //
    Gyroscope,                    //
    Light,                        //
    Proximity,                    //
    Gravity,                      //
    RotationVector,               //
    RotationAngles,               //
    RelativeHumidity,             //
    AmbientTemperature,           //
    GameRotationVector,           //
    GeomagneticRotationVector,    //
    HeartRate,                    //
    StationaryDetect,             //
    MotionDetect,                 //
    HeartBeat,                    //
    LowLatencyOffBodyDetect,      //
    HingeAngle,                   //
//    Heading,                      //
}


fun GetBattery(context: Context): Int {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val batLevel: Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    return batLevel
}

fun GetDateTime(context: Context): List<Int> {
    val sdf = SimpleDateFormat("dd:MM:yyyy:HH:mm:ss")
    val currentDateAndTime = sdf.format(Date())
    return currentDateAndTime.split(":").map { it.toInt() }
}

fun GetVolume(context: Context): Float {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    return currentVolume.toFloat() / maxVolume.toFloat()
}

fun GetDarkMode(context: Context): Int {
    val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) 1 else 0
}

fun GetCharging(context: Context): Int {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return if (batteryManager.isCharging) 1 else 0
}