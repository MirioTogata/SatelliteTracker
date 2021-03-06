package st.rendering;

import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGraphicsOpenGL;
import st.Earth;
import st.util.FMath;
import st.util.Ray;

public class Player {

    public static final float VFOV = FMath.PI / 3.0f;

    private float alt, lat, lon;
    boolean wDown, sDown, aDown, dDown;

    public Player() {
        alt = 20.0f;
        lat = 0.0f;
        lon = 0.0f;
    }

    public void applyTransform(PGraphics g) {
        PVector cam = Earth.cartesian(alt, lat, lon);
        g.camera(cam.x, cam.y, cam.z,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);
    }

    public void update(float dt) {
        if(wDown) {
            lat += dt * FMath.PI * 0.5f;
            lat = FMath.clamp(lat, -FMath.PI * 0.5f + 0.001f, FMath.PI * 0.5f - 0.001f);
        }
        if(sDown) {
            lat -= dt * FMath.PI * 0.5f;
            lat = FMath.clamp(lat, -FMath.PI * 0.5f + 0.001f, FMath.PI * 0.5f - 0.001f);
        }
        if(aDown) {
            lon -= dt * FMath.PI;
        }
        if(dDown) {
            lon += dt * FMath.PI;
        }

    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKey()){
            case 'w':
                wDown = true;
                break;
            case 's':
                sDown = true;
                break;
            case 'a':
                aDown = true;
                break;
            case 'd':
                dDown = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKey()){
            case 'w':
                wDown = false;
                break;
            case 's':
                sDown = false;
                break;
            case 'a':
                aDown = false;
                break;
            case 'd':
                dDown = false;
                break;
        }
    }

    public void mouseWheel(MouseEvent e) {
        alt += e.getCount() * 0.5f;
        alt = FMath.clamp(alt, 1.0f, 100.0f);
    }

}
