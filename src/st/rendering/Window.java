package st.rendering;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PShader;

import st.Earth;
import st.Satellite;
import st.SatelliteManager;
import st.ui.ControlPanel;

import javax.swing.*;

public class Window extends PApplet {

    public static boolean DEBUG = false;

    private static Window instance;
    public static Window getWnd() {
        return instance;
    }
    private JFrame frame;

    private Earth earth;
    private Player player;
    private long tstart, tlast;

    private PShader earthShader;

    private SatelliteManager satMgr;

    private ControlPanel controlPanel;

    @Override
    public void settings() {
        size(1280, 720, PConstants.P3D);
    }

    @Override
    public void setup() {
        instance = this;
        Satellite.init(g);

        earth = new Earth(g);
        player = new Player();

        g.perspective(Player.VFOV, width/(float)height, 0.1f, 100.0f);

        tstart = System.nanoTime();
        tlast = tstart;

        satMgr = new SatelliteManager();
        controlPanel = new ControlPanel(satMgr);

        frame = new JFrame("yay");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setContentPane(controlPanel);
        frame.pack();
        frame.setVisible(true);

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

        if(DEBUG){
            g.strokeWeight(1.0f);

            g.stroke(0xFFFF0000);
            g.line(0.0f, 0.0f, 0.0f, Earth.RADIUS * 2.0f, 0.0f, 0.0f);
            g.stroke(0xFF00FF00);
            g.line(0.0f, 0.0f, 0.0f, 0.0f, Earth.RADIUS * 2.0f, 0.0f);
            g.stroke(0xFF0000FF);
            g.line(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Earth.RADIUS * 2.0f);
        }
    }



    @Override
    public void keyPressed(KeyEvent event) {
        player.keyPressed(event);
        if (key == 'c') {
            println("Hej");
            frame.setVisible(true);
        }
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
        satMgr.mousePressed(g, e);
    }

    public float aspectRatio() {
        return width/(float)height;
    }



}
