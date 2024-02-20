package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object FluidCursorFunctions {

    fun simulateFluid(
        points: List<Offset>,
        targetPosition: Offset,
        requiredRadius: Double,
        deltaTime: Double,
        stiffness: Double
    ): List<Offset> {
        val updatedPoints = mutableListOf<Offset>()

        // Apply forces to simulate movement
        val forces = mutableListOf<Offset>()
        for (point in points) {
            val dx = targetPosition.x - point.x
            val dy = targetPosition.y - point.y
            val distance = sqrt(dx * dx + dy * dy)
            val angle = atan2(dy, dx)
            val displacement = distance - stiffness
            val force = Offset((displacement * cos(angle)).toFloat(), (displacement * sin(angle)).toFloat())
            forces.add(force)
        }

        // Verlet integration
        for ((index, point) in points.withIndex()) {
            val force = forces[index]
            val newPos = Offset(point.x + force.x, point.y + force.y)
            // Apply constraint to maintain required radius
            val distanceToCenter = sqrt((newPos.x - targetPosition.x).pow(2) + (newPos.y - targetPosition.y).pow(2))
            val scaleFactor = requiredRadius / distanceToCenter
            val constrainedPos = Offset((targetPosition.x + scaleFactor * (newPos.x - targetPosition.x)).toFloat(), (targetPosition.y + scaleFactor * (newPos.y - targetPosition.y)).toFloat())
            updatedPoints.add(constrainedPos)
        }
        return updatedPoints
    }
}