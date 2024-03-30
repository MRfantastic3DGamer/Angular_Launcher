package com.dhruv.angular_launcher.accessible_screen.components.glsl_art
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Size
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.core.resources.ShaderData
import com.dhruv.angular_launcher.utils.ScreenUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

const val MAX_ICONS = 100

class MyGLRenderer(
    private val resources: Resources,
    private val shaderData: ShaderData,
) : GLSurfaceView.Renderer {
    private var textureHandle: Int = 0
    private lateinit var bitmap: Bitmap

    private val vertexShaderCode =
"attribute vec4 aPosition;" +
"attribute vec2 aTexCoord;" +
"varying vec2 vTexCoord;" +
"void main() {" +
"   gl_Position = aPosition;" +
"   vTexCoord = aTexCoord / vec2(${ScreenUtils.screenSizeDp.height/ ScreenUtils.screenSizeDp.width}., 1.);" +
"}"

    private val fragmentShaderCode = shaderData.code

    private var program: Int = 0

    private var timeHandler: Int? = null
    private var resolutionHandler: Int? = null
    private var mouseHandler: Int? = null
    private var interactionHandler: Int? = null
    private var iconsXUniformLocation: Int? = null
    private var iconsYUniformLocation: Int? = null

    private var battery = 0
    private var dateAndTime = intArrayOf()
    private var frame = 0

    private var time = 0f
    private var resolution = Size(0,0)
    private var mousePos = -Offset.Infinite
    private var interactionPos = -Offset.Infinite
    private var iconsPosX = listOf<Float>()
    private var iconsPosY = listOf<Float>()

    fun interactionTrigger(pos: Offset) {
        interactionPos = pos
        time = 0f
    }

    fun updateMousePos(x: Float, y:Float) {
        mousePos = Offset(x, ScreenUtils.fromDown(y))
    }

    fun setIcons(positions: List<Offset>){
        val offsetsX = mutableListOf<Float>()
        val offsetsY = mutableListOf<Float>()
        for (i in 0 until MAX_ICONS){
            if (i in positions.indices){
                offsetsX.add(positions[i].x)
                offsetsY.add(positions[i].y)
            }
            else{
                offsetsX.add(-10000f)
                offsetsY.add(-10000f)
                break
            }
        }
        iconsPosX = offsetsX
        iconsPosY = offsetsY
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        shaderData.textures.forEach {
            val bitmap = BitmapFactory.decodeResource(resources, it.value)
            loadTexture(bitmap)
        }
        setupShader()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glDisable(GL10.GL_DITHER)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        timeHandler?.let { GLES20.glUniform1f(it, time) }
        resolutionHandler?.let { GLES20.glUniform2f(it, resolution.width.toFloat(), resolution.height.toFloat()) }
        mouseHandler?.let { GLES20.glUniform2f(it, mousePos.x, mousePos.y) }
        interactionHandler?.let { GLES20.glUniform2f(it, mousePos.x, mousePos.y) }
        iconsXUniformLocation?.let {
            for (i in 0 until MAX_ICONS) {
                if (i >= iconsPosX.size) break
                GLES20.glUniform1f(it + i, iconsPosX[i])
            }
        }
        iconsYUniformLocation?.let {
            for (i in 0 until  MAX_ICONS) {
                if (i >= iconsPosY.size) break
                GLES20.glUniform1f(it + i, iconsPosY[i])
            }
        }

        drawTexture()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        resolution = Size(width, height)
    }

    private fun loadTexture(bitmap: Bitmap) {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        textureHandle = textures[0]

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle)

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        bitmap.recycle()
    }

    private fun setupShader() {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        println(fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }

        timeHandler = GLES20.glGetUniformLocation(program, "u_time")
        resolutionHandler = GLES20.glGetUniformLocation(program, "u_resolution")
        mouseHandler = GLES20.glGetUniformLocation(program, "u_mouse")
        interactionHandler = GLES20.glGetUniformLocation(program, "u_interaction")
        iconsXUniformLocation = GLES20.glGetUniformLocation(program, "u_positions_X")
        iconsYUniformLocation = GLES20.glGetUniformLocation(program, "u_positions_Y")
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    private fun drawTexture() {
        GLES20.glUseProgram(program)

        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)

        val textureCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        GLES20.glEnableVertexAttribArray(textureCoordHandle)

        val texturesUniformHandle = shaderData.textures.map { it.key to GLES20.glGetUniformLocation(program, it.key) }.toMap()

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle)
        texturesUniformHandle.forEach { key, uniformLocation -> GLES20.glUniform1i(uniformLocation, 0) }

        // Define vertices and texture coordinates here and pass them to OpenGL ES buffers
        // Example vertices and texture coordinates
        val vertices = floatArrayOf(
            -1.0f, 1.0f,  // top left
            -1.0f, -1.0f, // bottom left
            1.0f, -1.0f,  // bottom right
            1.0f, 1.0f    // top right
        )

        val textureCoords = floatArrayOf(
            0.0f, 0.0f,   // top left
            0.0f, 1.0f,   // bottom left
            1.0f, 1.0f,   // bottom right
            1.0f, 0.0f    // top right
        )

        val vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

        val textureCoordBuffer = ByteBuffer.allocateDirect(textureCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(textureCoords)
                position(0)
            }
        }

        GLES20.glVertexAttribPointer(
            positionHandle, 2,
            GLES20.GL_FLOAT, false,
            0, vertexBuffer
        )

        GLES20.glVertexAttribPointer(
            textureCoordHandle, 2,
            GLES20.GL_FLOAT, false,
            0, textureCoordBuffer
        )

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4)

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(textureCoordHandle)
    }
    
}