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

    private Satellite sat;

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

        try {
                earthShader = loadShader(
                        new File(getClass().getResource("/shaders/frag_tex.glsl").toURI()).getAbsolutePath(),
                        new File(getClass().getResource("/shaders/vert_tex.glsl").toURI()).getAbsolutePath());

                shader(earthShader);

            } catch (URISyntaxException e) {
                e.printStackTrace();
        }

        tstart = System.nanoTime();
        tlast = tstart;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.n2yo.com/rest/v1/satellite/positions/25544/0/0/0/5/&apiKey=X7JFAR-LQFKV6-W5A39A-4KB1"))
                    .build();


            String s = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            JSONObject data = JSONObject.parse(s);

            JSONArray positions = (JSONArray)data.get("positions");

            sat = new Satellite(
                    g,
                    getCoords(positions.getJSONObject(0)),
                    getCoords(positions.getJSONObject(1)),
                    (positions.getJSONObject(0)).getInt("timestamp"),
                    (positions.getJSONObject(1)).getInt("timestamp")
                    );
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
        sat.update(dt);

        g.background(0.0f);
        player.applyTransform(g);

        g.ambientLight(255.0f, 255.0f, 255.0f);
        g.lightFalloff(1.0f, 0.000f, 0.0f);
        g.pointLight(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 20.0f);
        
        earth.draw(g);
        sat.draw(g);

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

    public static PVector getCoords(JSONObject json) {
        PVector coords = new PVector();
        coords.x = json.getFloat("sataltitude");
        coords.y = json.getFloat("satlatitude");
        coords.z = json.getFloat("satlongitude");

        return coords;
    }

}
