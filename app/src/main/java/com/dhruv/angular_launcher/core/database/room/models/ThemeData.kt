package com.dhruv.angular_launcher.core.database.room.models

import android.content.res.Resources
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MyGLRenderer
import com.dhruv.angular_launcher.core.appIcon.IconStyle
import com.dhruv.angular_launcher.core.resources.ShaderData
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationScheme

@Entity(tableName = "themes")
data class ThemeData(
    @PrimaryKey(autoGenerate = true) var _id: Int? = null,
    var name: String = "unnamed",

    // shader
    var shader: String = ShaderData().toString(),

    // cursor
    var cursorStyle: String = "",
    var freeRadius: Float = 0.07f,
    var appStuckRadius: Float = 0.15f,
    var sliderStuckRadius: Float = 0.15f,
    /*** at touch <--> bouncy*/
    var cursorSlidingMode: Int = 0,

    // slider

    var sliderWidth: Float = 50f,
    /** constant, adaptive*/
    var heightMode: Int = 0,
    var constantSliderHeight: Float = 500f,
    var perGroupSliderHeight: Float = 50f,
    /** to selection, animate to selection, to touch, animate to touch*/
    var sliderSlideMode: Int = 0,
    var sidePadding: Float = 40f,

    // constraints
    var topSliderLimit: Float = 50f,
    var downSliderLimit: Float = 50f,

    // looks
    var selectionCurveOffset:Float = 50f,

    // appsNavigator
    // look
    var iconsPositioningScheme1: String = IconCoordinatesGenerationScheme().toString(),
    var iconsPositioningScheme2: String = IconCoordinatesGenerationScheme().toString(),
    var iconsPositioningScheme3: String = IconCoordinatesGenerationScheme().toString(),
    var iconsPositioningScheme4: String = IconCoordinatesGenerationScheme().toString(),
    var iconsPositioningScheme5: String = IconCoordinatesGenerationScheme().toString(),

    /** on selection, closeness to cursor */
    var iconStyleTransitionMode: Int = 0,
    var navigationIconStyle: String = IconStyle().toString(),
    var navigationSelectedIconStyle: String = IconStyle().toString(),

    // label

    var labelIconStyle: String = IconStyle().toString(),
)

fun ThemeData.getIconPositioningSchemes(): List<IconCoordinatesGenerationScheme> = listOf(
    this.iconsPositioningScheme1,
    this.iconsPositioningScheme2,
    this.iconsPositioningScheme3,
    this.iconsPositioningScheme4,
    this.iconsPositioningScheme5,
).map { IconCoordinatesGenerationScheme.fromString(it) }

/**
 * navigationIconStyle,
 * navigationSelectedIconStyle,
 * labelIconStyle,
 */
fun ThemeData.getIconStyles(): List<IconStyle> = listOf(
    navigationIconStyle,
    navigationSelectedIconStyle,
    labelIconStyle,
).map { IconStyle.fromString(it) }

fun ThemeData.getCopy(): ThemeData = copy(
    _id = null,
    name = "copy $name",
)

fun ThemeData.getShader(): ShaderData = ShaderData.fromString(shader)

fun ThemeData.setShader(shader : ShaderData): ThemeData = copy(shader = shader.toString())

fun ThemeData.getSliderHeight(items: Int): Float = when (heightMode) {
    0 -> { perGroupSliderHeight * items }
    1 -> { constantSliderHeight }
    else -> {0f}
}

fun ThemeData.getRenderer(resources: Resources): MyGLRenderer = MyGLRenderer(resources, getShader())