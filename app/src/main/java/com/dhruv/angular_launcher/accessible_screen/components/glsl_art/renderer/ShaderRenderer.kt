package com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer

//import timber.log.Timber
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.GL_RGBA
import android.opengl.GLES20.GL_TRIANGLE_STRIP
import android.opengl.GLES20.GL_UNSIGNED_BYTE
import android.opengl.GLES20.GL_VALIDATE_STATUS
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glCreateProgram
import android.opengl.GLES20.glDeleteProgram
import android.opengl.GLES20.glDeleteShader
import android.opengl.GLES20.glDetachShader
import android.opengl.GLES20.glDisable
import android.opengl.GLES20.glDisableVertexAttribArray
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetProgramiv
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glHint
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLES20.glReadPixels
import android.opengl.GLES20.glUniform1f
import android.opengl.GLES20.glUniform2f
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glValidateProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import android.os.Trace
import androidx.compose.ui.geometry.Offset
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.helper.createAndVerifyShader
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicBoolean
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.roundToInt

open class ShaderRenderer : GLSurfaceView.Renderer {

    private val positionComponentCount = 2

    private val quadVertices by lazy {
        floatArrayOf(
            -1f, 1f,
            1f, 1f,
            -1f, -1f,
            1f, -1f
        )
    }

    private var surfaceHeight = 0f
    private var surfaceWidth = 0f

    private val bytesPerFloat = 4

    val max_icons = 100
    private var mouseX = 0f
    private var mouseY = 0f

    private var iconsPosX = listOf<Float>()
    private var iconsPosY = listOf<Float>()

    private val verticesData by lazy {
        ByteBuffer.allocateDirect(quadVertices.size * bytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().also {
                it.put(quadVertices)
            }
    }

    private var snapshotBuffer = initializeSnapshotBuffer(0, 0)


    fun updateMousePos(x: Float, y:Float) {
        mouseX = x
        mouseY = y
    }

    fun setIcons(positions: List<Offset>){
        val offsetsX = mutableListOf<Float>()
        val offsetsY = mutableListOf<Float>()
        for (i in 0 until max_icons){
            if (i in positions.indices){
                offsetsX.add(positions[i].x)
                offsetsY.add(positions[i].y)
            }
            else{
                offsetsX.add(-10000f)
                offsetsY.add(-10000f)
            }
        }
        iconsPosX = offsetsX
        iconsPosY = offsetsY
    }

    private fun initializeSnapshotBuffer(width: Int, height: Int) = ByteBuffer.allocateDirect(
        width *
                height *
                bytesPerFloat
    ).order(ByteOrder.nativeOrder())

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 1f)
        glDisable(GL10.GL_DITHER)
        glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
    }

    private val isProgramChanged = AtomicBoolean(false)

    private var programId: Int? = null

    private lateinit var fragmentShader: String
    private lateinit var vertexShader: String
    private lateinit var eventSource: String

    fun setShaders(fragmentShader: String, vertexShader: String, source: String) {
        this.fragmentShader = fragmentShader
        this.vertexShader = vertexShader
        this.eventSource = source
        shouldPlay.compareAndSet(false, true)
        isProgramChanged.compareAndSet(false, true)
    }

    private fun setupProgram() {
        programId?.let { glDeleteProgram(it) }

        programId = glCreateProgram().also { newProgramId ->
            if (programId == 0) {
//                Timber.d("Could not create new program")
                return
            }

            val fragShader = createAndVerifyShader(fragmentShader, GL_FRAGMENT_SHADER)
            val vertShader = createAndVerifyShader(vertexShader, GL_VERTEX_SHADER)

            glAttachShader(newProgramId, vertShader)
            glAttachShader(newProgramId, fragShader)

            glLinkProgram(newProgramId)

            val linkStatus = IntArray(1)
            glGetProgramiv(newProgramId, GL_LINK_STATUS, linkStatus, 0)

            if (linkStatus[0] == 0) {
                glDeleteProgram(newProgramId)
//                Timber.d("Linking of program failed. ${glGetProgramInfoLog(newProgramId)}")
                return
            }

            if (validateProgram(newProgramId)) {
                positionAttributeLocation = glGetAttribLocation(newProgramId, "a_Position")
                resolutionUniformLocation = glGetUniformLocation(newProgramId, "u_resolution")
                timeUniformLocation = glGetUniformLocation(newProgramId, "u_time")
                cursorUniformLocation = glGetUniformLocation(newProgramId, "u_mouse")
                iconsXUniformLocation = glGetUniformLocation(newProgramId, "u_positions_X")
                iconsYUniformLocation = glGetUniformLocation(newProgramId, "u_positions_Y")
            } else {
//                Timber.d("Validating of program failed.");
                return
            }

            verticesData.position(0)

            positionAttributeLocation?.let { attribLocation ->
                glVertexAttribPointer(
                    attribLocation,
                    positionComponentCount,
                    GL_FLOAT,
                    false,
                    0,
                    verticesData
                )
            }

            glDetachShader(newProgramId, vertShader)
            glDetachShader(newProgramId, fragShader)
            glDeleteShader(vertShader)
            glDeleteShader(fragShader)
            println("new program id: $newProgramId")
        }
        println("program id: $programId")
    }

    private var positionAttributeLocation: Int? = null
    private var resolutionUniformLocation: Int? = null
    private var timeUniformLocation: Int? = null
    private var cursorUniformLocation: Int? = null
    private var iconsXUniformLocation: Int? = null
    private var iconsYUniformLocation: Int? = null


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        snapshotBuffer = initializeSnapshotBuffer(width, height)
        surfaceWidth = width.toFloat()
        surfaceHeight = height.toFloat()
        frameCount = 0f
    }

    private var frameCount = 0f

    override fun onDrawFrame(gl: GL10?) {
        if (shouldPlay.get()) {
            Trace.beginSection(eventSource)
            glDisable(GL10.GL_DITHER)
            glClear(GL10.GL_COLOR_BUFFER_BIT)


            if (isProgramChanged.getAndSet(false)) {
                setupProgram()
            } else {
                programId?.let {
                    glUseProgram(it)
                } ?: return
            }

            positionAttributeLocation?.let {
                glEnableVertexAttribArray(it)
            } ?: return


            resolutionUniformLocation?.let {
                glUniform2f(it, surfaceWidth, surfaceHeight)
            }

            timeUniformLocation?.let {
                glUniform1f(it, frameCount)
            }

            cursorUniformLocation?.let {
                glUniform2f(it, mouseX, mouseY)
            }

            iconsXUniformLocation?.let {
                if (iconsPosX.size == max_icons) {
                    for (i in 0 until max_icons) {
                        glUniform1f(it + i, iconsPosX[i])
                    }
                }
            }

            iconsYUniformLocation?.let {
                if (iconsPosY.size == max_icons){
                    for (i in 0 until  max_icons) {
                        glUniform1f(it + i, iconsPosY[i])
                    }
                }
            }

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)

            positionAttributeLocation?.let {
                glDisableVertexAttribArray(it)
            } ?: return

            getPaletteCallback?.let { callback ->
                if (surfaceWidth != 0f && surfaceHeight != 0f) {
                    getCurrentBitmap()?.let { bitmap ->
                        Palette.Builder(bitmap)
                            .maximumColorCount(6)
                            .addTarget(Target.VIBRANT)
                            .generate().let { palette ->
                                callback(palette)
                                getPaletteCallback = null
                                bitmap.recycle()
                            }
                    }
                }
            }

            frameCount = (frameCount + 0.01f)%6

            Trace.endSection()
        }
    }

    private fun getCurrentBitmap(): Bitmap? {
        val maxWidth = surfaceWidth.roundToInt()
        val maxHeight = surfaceHeight.roundToInt()

        val quarterWidth = maxWidth / 6
        val quarterHeight = maxHeight / 6

        val halfWidth = quarterWidth * 2
        val halfHeight = quarterHeight * 2

        initializeSnapshotBuffer(
            halfWidth * 2,
            halfHeight * 2,
        )

        glReadPixels(
            halfWidth,
            halfHeight,
            halfWidth * 2,
            halfHeight * 2,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            snapshotBuffer
        )

        val bitmap = Bitmap.createBitmap(
            24,
            24,
            Bitmap.Config.ARGB_8888
        )

        bitmap.copyPixelsFromBuffer(snapshotBuffer)
        return bitmap
    }

    private fun validateProgram(programObjectId: Int): Boolean {
        glValidateProgram(programObjectId)
        val validateStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0)

        return validateStatus[0] != 0
    }

    private var getPaletteCallback: ((Palette) -> Unit)? = null

    fun setPaletteCallback(callback: (Palette) -> Unit) {
        getPaletteCallback = callback
    }

    private val shouldPlay = AtomicBoolean(false)

    fun onResume() {
        shouldPlay.compareAndSet(false, ::fragmentShader.isInitialized)
    }

    fun onPause() {
        shouldPlay.compareAndSet(true, false)
    }

}

fun Context.readTextFileFromResource(
    resourceId: Int
): String {
    val body = StringBuilder()
    try {
        val inputStream = resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var nextLine: String?
        while (bufferedReader.readLine().also { nextLine = it } != null) {
            body.append(nextLine)
            body.append('\n')
        }
    } catch (e: IOException) {
        throw RuntimeException(
            "Could not open resource: $resourceId", e
        )
    } catch (nfe: Resources.NotFoundException) {
        throw RuntimeException("Resource not found: $resourceId", nfe)
    }
    return body.toString()
}