package com.dhruv.angular_launcher.utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object MathUtils {
    fun calculateAngle(a: Offset, b: Offset, c: Offset): Float {
        val ab: Double = sqrt((b.x - a.x).toDouble().pow(2.0) + (b.y - a.y).toDouble().pow(2.0))
        val bc: Double = sqrt((c.x - b.x).toDouble().pow(2.0) + (c.y - b.y).toDouble().pow(2.0))
        val ac: Double = sqrt((c.x - a.x).toDouble().pow(2.0) + (c.y - a.y).toDouble().pow(2.0))
        val ratio : Double = (ab * ab + ac * ac - bc * bc) /( 2 * ac * ab)
        val degree = acos(ratio) *(180/Math.PI)
        return degree.toFloat()
    }

    fun calculateDistance(a: Offset, b: Offset): Float{
        val X = b.x - a.x
        val Y = b.y - a.y
        return sqrt((X*X) + (Y*Y))
    }

    fun calculateAngleOnCircle(radius: Double, distance: Double): Double {
        if (radius <= 0.0 || distance <= 0.0) {
            throw IllegalArgumentException("Both radius and distance must be positive values , radius:${radius}, distance:${distance}")
        }
        val angleInRadians = distance / radius
        val angleInDegrees = Math.toDegrees(angleInRadians)
        return angleInDegrees
    }

    fun getPositionOnCircle(radius: Double, angleDegrees: Double): Offset {
        if (radius <= 0.0) {
            throw IllegalArgumentException("Radius must be a positive value")
        }
        val angleRadians = Math.toRadians(angleDegrees)
        val x = radius * cos(angleRadians)
        val y = radius * sin(angleRadians)

        return Offset(x.toFloat(), y.toFloat())
    }


    data class Rectangle(val x: Float, val y: Float, val width: Float, val height: Float)
    data class Circle(val x: Float, val y: Float, val radius: Float)
    data class intersections(var t: Offset? = null, var b: Offset? = null, var l1: Offset? = null, var l2: Offset? = null, var r1: Offset? = null, var r2: Offset? = null)
    fun intersectCircleRectangle(circle: Circle, rectangle: Rectangle): intersections {

        val res = intersections()

        val left = rectangle.x
        val right = rectangle.x + rectangle.width
        val top = rectangle.y
        val bottom = rectangle.y + rectangle.height

        val centerX = circle.x
        val centerY = circle.y
        val radius = circle.radius

        // region Right side
        val ry = sqrt(radius * radius - (right - centerX) * (right - centerX))
        val rightIntersect1 = Offset(
            x = right,
            y = centerY + ry
        )
        if (rightIntersect1.y in top..bottom) {
            res.r1 = rightIntersect1
        }

        val rightIntersect2 = Offset(
            x = right,
            y = centerY - ry
        )
        if (rightIntersect2.y in top..bottom) {
            res.r2 = rightIntersect2
        }
        // endregion

        // region Left side
        val ly = sqrt(radius * radius - (left - centerX) * (left - centerX))
        val leftIntersect1 = Offset(
            x = left,
            y = centerY + ly
        )
        if (leftIntersect1.y in top..bottom) {
            res.l1 = leftIntersect1
        }

        val leftIntersect2 = Offset(
            x = left,
            y = centerY - ly
        )
        if (leftIntersect2.y in top..bottom) {
            res.l2 = leftIntersect2
        }
        // endregion

        // Find the intersection points with the sides of the rectangle
        // Top side
        val topIntersect = Offset(
            x = centerX + sqrt(radius * radius - (top - centerY) * (top - centerY)),
            y = top
        )
        if (topIntersect.x in left..right) {
            res.t = topIntersect
        }

        // Bottom side
        val bottomIntersect = Offset(
            x = centerX - sqrt(radius * radius - (bottom - centerY) * (bottom - centerY)),
            y = bottom
        )
        if (bottomIntersect.x in left..right) {
            res.b = bottomIntersect
        }

        return res
    }

    fun arcLength (center: Offset, A:Offset, B:Offset, radius: Float): Float {
        return 2 * PI.toFloat() * radius * calculateAngle(A, center, B) / 360f
    }

    fun iconsPossible (center: Offset, A:Offset, B:Offset, radius: Float, iconRadius: Float, distanceBWIcons: Float): Int {
        return (arcLength(center, A, B, radius) / (iconRadius + distanceBWIcons)).toInt()
    }

}