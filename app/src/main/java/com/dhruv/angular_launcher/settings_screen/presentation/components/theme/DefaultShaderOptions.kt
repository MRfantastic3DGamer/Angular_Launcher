package com.dhruv.angular_launcher.settings_screen.presentation.components.theme

object DefaultShaderOptions {
    val blobs_Apps: String =
        """
        // starting point
        void main() {
        // distance from cursor
            float value = 1./distance(gl_FragCoord.xy, u_mouse) * 1.2;
            vec2 t_pos = vec2(0., 0.);
            float t_dist = 100000.;
        
            for(int i=0;i<com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MAX_ICONS;++i)
            {
                if (u_positions_X[i] == -10000.)
                    break;
                t_pos = vec2(u_positions_X[i], u_positions_Y[i]);
        // distance from closest icon
                t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));
            }
            value += 1./t_dist;
        
            float insideCircle = step(value, 0.02);
        
        // set black where [value] is low
            vec4 color = insideCircle * vec4(0.0, 0.0, 0.0, 0.0);
        // set white where [value] is high
            color += (1.0 - insideCircle) * vec4(1.0, 1.0, 1.0, 1.0);
        // set the color
            gl_FragColor = color;
        }
        """
}