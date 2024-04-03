package com.dhruv.angular_launcher.accessible_screen.components.glsl_art

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.dhruv.angular_launcher.core.resources.AllResources
import com.dhruv.angular_launcher.core.resources.GetBattery
import com.dhruv.angular_launcher.core.resources.GetCharging
import com.dhruv.angular_launcher.core.resources.GetDarkMode
import com.dhruv.angular_launcher.core.resources.GetDateTime
import com.dhruv.angular_launcher.core.resources.GetVolume
import com.dhruv.shader_test.opengl_renderer.ui.MyGLSurfaceView
import com.mutualmobile.composesensors.rememberAccelerometerSensorState
import com.mutualmobile.composesensors.rememberAmbientTemperatureSensorState
import com.mutualmobile.composesensors.rememberGameRotationVectorSensorState
import com.mutualmobile.composesensors.rememberGeomagneticRotationVectorSensorState
import com.mutualmobile.composesensors.rememberGravitySensorState
import com.mutualmobile.composesensors.rememberGyroscopeSensorState
import com.mutualmobile.composesensors.rememberHeartBeatSensorState
import com.mutualmobile.composesensors.rememberHeartRateSensorState
import com.mutualmobile.composesensors.rememberHingeAngleSensorState
import com.mutualmobile.composesensors.rememberLightSensorState
import com.mutualmobile.composesensors.rememberLowLatencyOffBodyDetectSensorState
import com.mutualmobile.composesensors.rememberMagneticFieldSensorState
import com.mutualmobile.composesensors.rememberMotionDetectSensorState
import com.mutualmobile.composesensors.rememberProximitySensorState
import com.mutualmobile.composesensors.rememberRelativeHumiditySensorState
import com.mutualmobile.composesensors.rememberRotationVectorSensorState
import com.mutualmobile.composesensors.rememberStationaryDetectSensorState

@Composable
fun MyGLSurfaceContent(
    renderer: MyGLRenderer
) {
    val context = LocalContext.current

    val AccelerometerState = rememberAccelerometerSensorState()
    val MagneticFieldState = rememberMagneticFieldSensorState()
    val GyroscopeState = rememberGyroscopeSensorState()
    val LightState = rememberLightSensorState()
    val ProximityState = rememberProximitySensorState()
    val GravityState = rememberGravitySensorState()
    val RotationVectorState = rememberRotationVectorSensorState()
    val RelativeHumidityState = rememberRelativeHumiditySensorState()
    val AmbientTemperatureState = rememberAmbientTemperatureSensorState()
    val GameRotationVectorState = rememberGameRotationVectorSensorState()
    val GeomagneticRotationVectorState = rememberGeomagneticRotationVectorSensorState()
    val HeartRateState = rememberHeartRateSensorState()
    val StationaryDetectState = rememberStationaryDetectSensorState()
    val MotionDetectState = rememberMotionDetectSensorState()
    val HeartBeatState = rememberHeartBeatSensorState()
    val LowLatencyOffBodyDetectState = rememberLowLatencyOffBodyDetectSensorState()
    val HingeAngleState = rememberHingeAngleSensorState()
//    val HeadingState = rememberHeadingSensorState()

    val requirements = renderer.shaderData.resourcesAsked.mapNotNull {
        try {
            enumValueOf<AllResources>(it)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    requirements.forEach{ req ->
        when (req) {

            // from app
            AllResources.BackBuffer,
            AllResources.Frame,
            AllResources.TouchPosition,
            AllResources.TouchStartPositions,
            AllResources.IconsPositions,
            AllResources.IconsCount,
            AllResources.Resolution -> {}

            // from functions
            AllResources.Battery -> {
                renderer.PrepareData(AllResources.Battery.name, GetBattery(context))
            }
            AllResources.DateTime -> {
                renderer.PrepareData(AllResources.DateTime.name, GetDateTime(context))
            }
            AllResources.DarkMode -> {
                renderer.PrepareData(AllResources.DarkMode.name, GetDarkMode(context))
            }
            AllResources.Volume -> {
                renderer.PrepareData(AllResources.Volume.name, GetVolume(context))
            }
            AllResources.Charging -> {
                renderer.PrepareData(AllResources.Charging.name, GetCharging(context))
            }
            // by sensors
            AllResources.Accelerometer -> {
                renderer.PrepareData(AllResources.Accelerometer.name, floatArrayOf(AccelerometerState.xForce, AccelerometerState.yForce, AccelerometerState.zForce))
            }
            AllResources.MagneticField -> {
                renderer.PrepareData(AllResources.MagneticField.name, floatArrayOf(MagneticFieldState.xStrength, MagneticFieldState.yStrength, MagneticFieldState.zStrength))
            }
            AllResources.Gyroscope -> {
                renderer.PrepareData(AllResources.Gyroscope.name, floatArrayOf(GyroscopeState.xRotation, GyroscopeState.yRotation, GyroscopeState.zRotation))
            }
            AllResources.Light -> {
                renderer.PrepareData(AllResources.Light.name, floatArrayOf(LightState.illuminance))
            }
            AllResources.Proximity -> {
                renderer.PrepareData(AllResources.Proximity.name, floatArrayOf(ProximityState.sensorDistance))
            }
            AllResources.Gravity -> {
                renderer.PrepareData(AllResources.Gravity.name, floatArrayOf(GravityState.xForce, GravityState.yForce, GravityState.zForce))
            }
            AllResources.RotationVector -> {
                renderer.PrepareData(AllResources.RotationVector.name, floatArrayOf(RotationVectorState.vectorX, RotationVectorState.vectorY, RotationVectorState.vectorZ))
            }
            AllResources.RelativeHumidity -> {
                renderer.PrepareData(AllResources.RelativeHumidity.name, floatArrayOf(RelativeHumidityState.absoluteHumidity.toFloat(), RelativeHumidityState.relativeHumidity, RelativeHumidityState.dewPointTemperature.toFloat(), RelativeHumidityState.actualTemp))
            }
            AllResources.AmbientTemperature -> {
                renderer.PrepareData(AllResources.AmbientTemperature.name, AmbientTemperatureState.temperature)
            }
            AllResources.GameRotationVector -> {
                renderer.PrepareData(AllResources.GameRotationVector.name, floatArrayOf(GameRotationVectorState.vectorX, GameRotationVectorState.vectorY, GameRotationVectorState.vectorZ))
            }
            AllResources.GeomagneticRotationVector -> {
                renderer.PrepareData(AllResources.GeomagneticRotationVector.name, floatArrayOf(GeomagneticRotationVectorState.vectorX, GeomagneticRotationVectorState.vectorY, GeomagneticRotationVectorState.vectorZ))
            }
            AllResources.HeartRate -> {
                renderer.PrepareData(AllResources.HeartRate.name, HeartRateState.heartRate)
            }
            AllResources.StationaryDetect -> {
                renderer.PrepareData(AllResources.StationaryDetect.name, StationaryDetectState.isDeviceStationary)
            }
            AllResources.MotionDetect -> {
                renderer.PrepareData(AllResources.MotionDetect.name, MotionDetectState.isDeviceInMotion)
            }
            AllResources.HeartBeat -> {
                renderer.PrepareData(AllResources.HeartBeat.name, HeartBeatState.isConfidentPeak)
            }
            AllResources.LowLatencyOffBodyDetect -> {
                renderer.PrepareData(AllResources.LowLatencyOffBodyDetect.name, LowLatencyOffBodyDetectState.isDeviceOnBody)
            }
            AllResources.HingeAngle -> {
                renderer.PrepareData(AllResources.HingeAngle.name, floatArrayOf(HingeAngleState.angle))
            }
//            AllResources.Heading -> {
//                renderer.PrepareData(AllResources.Heading.name, floatArrayOf(HeadingState.degrees))
//            }
        }
    }

    AndroidView(
        factory = { context ->
            MyGLSurfaceView(context, renderer).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}