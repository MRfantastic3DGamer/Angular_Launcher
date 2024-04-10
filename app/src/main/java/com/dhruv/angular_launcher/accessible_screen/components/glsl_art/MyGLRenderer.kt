package com.dhruv.angular_launcher.accessible_screen.components.glsl_art
import android.content.ContentResolver
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.core.resources.AllResources
import com.dhruv.angular_launcher.core.resources.ShaderData
import com.dhruv.angular_launcher.core.resources.textureBitmaps
import com.dhruv.angular_launcher.utils.ScreenUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.min

class MyGLRenderer(
    contentResolver: ContentResolver,
    val shaderData: ShaderData,
) : GLSurfaceView.Renderer {

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

    private val uniformLocations: MutableMap<String, Int?> = mutableMapOf()
    private val uniformValues: MutableMap<String, Any> = mutableMapOf()

    private val textures: List<Pair<String, Bitmap>> = shaderData.textureBitmaps(contentResolver)
    private val textureIds: IntArray = IntArray(textures.size)

    private var frame = 0
    private var resolution = floatArrayOf(0f,0f)

    fun PrepareData(key: String, value: Any){
        uniformValues[key] = value
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        setupShader()
        loadTextures()
    }

    private fun setupShader() {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }

        shaderData.resourcesAsked.forEach { uniformLocations[it] = GLES20.glGetUniformLocation(program, it) }
        textures.forEach { (name, bitmap) -> uniformLocations[name] = GLES20.glGetUniformLocation(program, name) }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    private fun loadTextures() {
        GLES20.glGenTextures(textures.size, textureIds, 0) // Generate texture IDs

        for (i in textures.indices) {
            val bitmap = textures[i].second
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[i])
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glDisable(GL10.GL_DITHER)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        if (uniformLocations.containsKey(AllResources.Frame.name)){
            GLES20.glUniform1i(uniformLocations[AllResources.Frame.name]!!, frame)
            frame += 1
        }
        uniformLocations.forEach{(key, location) ->
            location?.let { l ->
                uniformValues.getOrDefault(key, null)?.let { value ->
                    insertData(l, value)
                }
            }
        }

        for (i in textures.indices) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[i])
            GLES20.glUniform1i(GLES20.glGetUniformLocation(program, textures[i].first), i)
            println("name: ${textures[i].first} location: ${GLES20.glGetUniformLocation(program, textures[i].first)}, id: ${textureIds[i]}")
        }
        drawTexture()
    }

    private fun insertData(l: Int, value: Any){
        if (value is Float) GLES20.glUniform1f(l, value)
        if (value is FloatArray) {
            when (value.size) {
                1 -> {GLES20.glUniform1f(l, value[0])}
                2 -> {GLES20.glUniform2f(l, value[0], value[1])}
                3 -> {GLES20.glUniform3f(l, value[0], value[1], value[2])}
                4 -> {GLES20.glUniform4f(l, value[0], value[1], value[2], value[3])}
                else -> {}
            }
        }
        if (value is Int) GLES20.glUniform1i(l, value)
        if (value is IntArray) {
            when (value.size) {
                1 -> {GLES20.glUniform1i(l, value[0])}
                2 -> {GLES20.glUniform2i(l, value[0], value[1])}
                3 -> {GLES20.glUniform3i(l, value[0], value[1], value[2])}
                4 -> {GLES20.glUniform4i(l, value[0], value[1], value[2], value[3])}
                6 -> {
                    GLES20.glUniform3i(l, value[0], value[1], value[2])
                    GLES20.glUniform3i(l+3, value[3], value[4], value[5])
                }
                else -> {}
            }
        }
        if (value is Boolean) GLES20.glUniform1i(l, if (value) 1 else 0)
        if (value is List<*>) {
            val len = value.size
            if (len > 0 && value[0] is Offset) {
                for (i in 0 until  min(len, 100)) {
                    val offset = value[i] as Offset
                    GLES20.glUniform2f(l + i, offset.x, offset.y)
                }
            }
            if (len > 0 && value[0] is FloatArray) {
                if ((value[0] as FloatArray).size == 1)
                    for (i in 0 until  min(len, 100)) {
                        val v = value[i] as FloatArray
                        GLES20.glUniform1f(l + i, v[0])
                    }
                if ((value[0] as FloatArray).size == 2)
                    for (i in 0 until  min(len, 100)) {
                        val v = value[i] as FloatArray
                        GLES20.glUniform2f(l + i, v[0], v[1])
                    }
                if ((value[0] as FloatArray).size == 3)
                    for (i in 0 until  min(len, 100)) {
                        val v = value[i] as FloatArray
                        GLES20.glUniform3f(l + i, v[0], v[1], v[2])
                    }
                if ((value[0] as FloatArray).size == 4)
                    for (i in 0 until  min(len, 100)) {
                        val v = value[i] as FloatArray
                        GLES20.glUniform4f(l + i, v[0], v[1], v[2], v[3])
                    }
            }
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        resolution = floatArrayOf(width.toFloat(), height.toFloat())
        GLES20.glViewport(0, 0, width, height)
    }

    private fun drawTexture() {
        GLES20.glUseProgram(program)

        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)

        val textureCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        GLES20.glEnableVertexAttribArray(textureCoordHandle)

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