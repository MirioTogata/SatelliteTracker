package st.util;

import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import st.rendering.Player;
import st.rendering.Window;

public class Ray {
    public PVector ori, dir;

    public Ray(PVector ori, PVector dir) {
        this.ori = ori;
        this.dir = dir;
    }

    public Ray(PGraphics g, int x, int y) {
        final Window wnd = Window.getWnd();

        final float hRatio = (x + 0.5f) / wnd.width;
        final float vRatio = (y + 0.5f) / wnd.height;

        final float x_ = FMath.tan(Player.VFOV/2.0f) * (2.0f * hRatio - 1.0f) * wnd.aspectRatio();
        final float y_ = FMath.tan(Player.VFOV/2.0f) * (2.0f * vRatio - 1.0f);

        PMatrix3D mat = ((PGraphicsOpenGL)g).cameraInv.get();
        ori = mat.mult(new PVector(0.0f, 0.0f, 0.0f), null);

        mat.m03 = 0.0f;
        mat.m13 = 0.0f;
        mat.m23 = 0.0f;

        dir = mat.mult(new PVector(x_, y_, -1.0f).normalize(), null);
    }
}
