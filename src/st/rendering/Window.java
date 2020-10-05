package st.rendering;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PShader;
import st.Earth;
import st.Satellite;
import st.SatelliteManager;
import st.util.FMath;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Window extends PApplet {

    private static Window instance;
    public static Window getWnd() {
        return instance;
    }

    private Earth earth;
    private Player player;
    private long tstart, tlast;

    private PShader earthShader;

    private SatelliteManager satMgr;

    @Override
    public void settings() {
        size(1280, 720, PConstants.P3D);
    }

    @Override
    public void setup() {
        instance = this;

        earth = new Earth(g);
        player = new Player();

        g.perspective(Player.VFOV, width/(float)height, 0.1f, 100.0f);

        tstart = System.nanoTime();
        tlast = tstart;

        satMgr = new SatelliteManager();

        try {
            int[] noradids = new int[24];

            for(int i = 0; i < 24; i++){
                noradids[i] = 44914 + i;
            }

            //satMgr.track(g, noradids);
            satMgr.track(g, 25544);

        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void draw() {


        long tnow = System.nanoTime();
        float t = (tnow - tstart) * 1e-9f;
        float dt = (tnow - tlast) * 1e-9f;
        tlast = tnow;

        player.update(dt);
        satMgr.update(dt);

        g.background(0.0f);
        player.applyTransform(g);

        g.ambientLight(255.0f, 255.0f, 255.0f);
        g.lightFalloff(1.0f, 0.000f, 0.0f);
        g.pointLight(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 20.0f);
        
        earth.draw(g);
        satMgr.draw(g);

        strokeWeight(1.0f);

        stroke(0xFFFF0000);
        line(0.0f, 0.0f, 0.0f, Earth.RADIUS * 2.0f, 0.0f, 0.0f);
        stroke(0xFF00FF00);
        line(0.0f, 0.0f, 0.0f, 0.0f, Earth.RADIUS * 2.0f, 0.0f);
        stroke(0xFF0000FF);
        line(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Earth.RADIUS * 2.0f);
    }


    @Override
    public void keyPressed(KeyEvent event) {
        player.keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        player.keyReleased(event);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        player.mouseWheel(event);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.getRay(g, e.getX(), e.getY());
    }

    public float aspectRatio() {
        return width/(float)height;
    }

}
