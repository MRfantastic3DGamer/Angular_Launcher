package com.dhruv.angular_launcher.core.database.prefferences

data class ShaderData(
    val name: String,
    val textures: Map<String, Int>,
    val code: String =
        """
void main() {
    color += vec4(1.0, 1.0, 1.0, 1.0);
    gl_FragColor = color;
}
"""
) {
    companion object {
        private const val DELIMITER = "|"
        private const val EMPTY_MAP = "EMPTY_MAP"

        fun fromString(str: String): ShaderData {
            val parts = str.split(DELIMITER)
            require(parts.size == 3) { "Invalid string format for ShaderData : $str" }

            val name = parts[0]
            val texturesStr = parts[1]
            val textures = if (texturesStr == EMPTY_MAP) {
                emptyMap()
            } else {
                texturesStr.split(",").map { texture ->
                    val (key, value) = texture.split("=")
                    key to value.toInt()
                }.toMap()
            }

            val code = parts[2]
            return ShaderData(name, textures, code)
        }
    }

    override fun toString(): String {
        val texturesStr = if (textures.isEmpty()) {
            EMPTY_MAP
        } else {
            textures.entries.joinToString(",") { "${it.key}=${it.value}" }
        }
        return "$name$DELIMITER$texturesStr$DELIMITER$code"
    }
}