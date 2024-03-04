package com.dhruv.angular_launcher.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object MathUtils {

    fun calculateAngle(point1: Offset, point2: Offset, point3: Offset): Float {
        // Vector between point2 and point1
        val vec1X = point1.x - point2.x
        val vec1Y = point1.y - point2.y

        // Vector between point2 and point3
        val vec2X = point3.x - point2.x
        val vec2Y = point3.y - point2.y

        // Calculate dot product of vectors
        val dotProduct = vec1X * vec2X + vec1Y * vec2Y

        // Calculate cross product of vectors
        val crossProduct = vec1X * vec2Y - vec1Y * vec2X

        // Calculate the magnitude of vectors
        val magnitudeVec1 = sqrt(vec1X * vec1X + vec1Y * vec1Y)
        val magnitudeVec2 = sqrt(vec2X * vec2X + vec2Y * vec2Y)

        // Calculate the angle between vectors using atan2 function
        val angleRadians = atan2(crossProduct, dotProduct)

        // Convert radians to degrees
        var angleDegrees = Math.toDegrees(angleRadians.toDouble()).toFloat()

        // Ensure the angle is between 0 and 360 degrees
        angleDegrees = if (angleDegrees < 0) angleDegrees + 360f else angleDegrees

        return angleDegrees
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

    /**
     * @param a -> lifted point
     * @param k -> steepness
     */
    fun gaussian(x: Float, a: Float, k: Float): Float {
        return exp(-((x - a).pow(2) / (2 * k.pow(2))))
    }
}
