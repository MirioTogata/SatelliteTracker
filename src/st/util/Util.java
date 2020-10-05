package st.util;

import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class Util {


    public static void toRasterSpace(PGraphics g, PVector pos) {

        PGraphics3D g3d = (PGraphics3D)g;

        g3d.camera.mult(pos, pos);
        pos.x /= -pos.z;
        pos.y /= -pos.z;

        float canvasW = 2.0f;
        float canvasH = 2.0f / g3d.cameraAspect;

        pos.x = (pos.x + canvasW/2.0f) / canvasW;
        pos.y = (pos.y + canvasH/2.0f) / canvasH;

        pos.x *= g.width;
        pos.y *= g.height;

    }
}
