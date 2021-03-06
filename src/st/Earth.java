package st;

import processing.core.*;
import processing.opengl.PShader;
import st.rendering.Window;
import st.util.FMath;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Earth {

    // earths radius in mega meters (10^6 meters)
    public static final float RADIUS = 12.742f / 2.0f;
    // earths mass in yotta gram (10^24 grams)
    public static final float MASS = 5.9722e3f;

    private static PShader shader;

    private PShape shape;

    public Earth(PGraphics g) {

        if(shader == null){
            try {
                shader = g.loadShader(
                        new File(getClass().getResource("/shaders/frag_tex.glsl").toURI()).getAbsolutePath(),
                        new File(getClass().getResource("/shaders/vert_tex.glsl").toURI()).getAbsolutePath());

                //g.shader(shader);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


        }

        try {
            shape = g.loadShape(new File(getClass().getResource("/models/earth.obj").toURI()).getAbsolutePath());

            PImage tex = new PImage(ImageIO.read(new File(getClass().getResource("/textures/earth.png").toURI())));
            /*tex.loadPixels();
            tex.resize(1<<13, 1<<12);
            for(int i = 0; i < tex.width; i++){
                for (int j = 0; j < tex.height / 2; j++) {
                    int col = tex.pixels[i + j * tex.width];
                    tex.pixels[i + j * tex.width] = tex.pixels[i + (tex.height - 1 - j) * tex.width];
                    tex.pixels[i + (tex.height - 1 - j) * tex.width] = col;
                }
            }
            tex.updatePixels();
            tex.save("C:\\users\\valde\\projects\\SatelliteTracker\\resources\\textures\\earth.png");*/

            shape.setTexture(tex);

        } catch (IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }

        shape.setStroke(false);

    }

    public void draw(PGraphics g) {

        g.pushMatrix();
        g.scale(Earth.RADIUS);
        g.shape(shape);

        g.popMatrix();
    }

    public static PVector cartesian(PVector coords) {
        return cartesian(coords.x, coords.y, coords.z);
    }

    public static PVector cartesian(float alt, float lat, float lon) {

        alt += Earth.RADIUS;
        lat = -lat;
        lon = -lon;

        float polar = FMath.PI/2.0f - lat;

        return new PVector(
                alt * FMath.cos(lon) * FMath.sin(polar),
                alt * FMath.cos(polar),
                alt * FMath.sin(lon) * FMath.sin(polar)
        );
    }
}
