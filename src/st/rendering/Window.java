package st.rendering;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.opengl.PShader;
import st.Earth;

import java.awt.*;

public class Window extends PApplet {



    private static Window instance;
    public static Window getWnd() {
        return instance;
    }

    private Earth earth;
    private Player player;
    long tstart, tlast;

    @Override
    public void settings() {
        size(1280, 720, PConstants.P3D);
    }

    @Override
    public void setup() {
        instance = this;

        earth = new Earth(g);
        player = new Player(20.0f);

        g.perspective(PI / 3.0f, width/(float)height, 0.1f, 100.0f);

        tstart = System.nanoTime();
        tlast = tstart;
    }

    @Override
    public void draw() {

        long tnow = System.nanoTime();
        float t = (tnow - tstart) * 1e-9f;
        float dt = (tnow - tlast) * 1e-9f;
        tlast = tnow;

        player.update(dt);

        g.background(0.0f);
        player.applyTransform(g);

        g.pushMatrix();

        earth.draw(g);
        g.popMatrix();
    }


    @Override
    public void keyPressed(KeyEvent event) {
        player.keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        player.keyReleased(event);
    }
}
