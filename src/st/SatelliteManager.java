package st;

import jdk.jshell.SourceCodeAnalysis;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import st.util.FMath;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SatelliteManager {

    private HttpClient httpClient;
    private HashMap<Integer, Satellite> sats;


    public SatelliteManager() {
        sats = new HashMap<>();

        httpClient = HttpClient.newHttpClient();
    }

    public void track(PGraphics g, int... noradids) throws IOException, InterruptedException {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < noradids.length; i++) {
            final int norad = noradids[i];

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.n2yo.com/rest/v1/satellite/positions/" + norad + "/0/0/0/300/&apiKey=X7JFAR-LQFKV6-W5A39A-4KB1"))
                    .build();

            futures.add(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(json -> {
                        JSONObject data = JSONObject.parse(json);
                        JSONArray positions = (JSONArray) data.get("positions");

                        Vector<PVector> targets = new Vector<>();
                        for (int j = 0; j < 300; j++) {
                            targets.add(Earth.cartesian(getCoords(positions.getJSONObject(j))));
                        }

                        long unixTimeFirst = positions.getJSONObject(0).getInt("timestamp");

                        Satellite sat = new Satellite(g, targets, unixTimeFirst);

                        synchronized (sats) {
                            sats.put(norad, sat);
                        }
                    }));
        }

        futures.forEach(f -> f.join());

    }

    public void update(float dt) {
        final long unixTimeMillis = System.currentTimeMillis();
        final long unixTime = unixTimeMillis / 1000L;

        sats.forEach((norad, sat) -> {

            boolean needsRefresh = sat.update(unixTime, unixTimeMillis);
            if(needsRefresh) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://www.n2yo.com/rest/v1/satellite/positions/" + norad + "/0/0/0/300/&apiKey=X7JFAR-LQFKV6-W5A39A-4KB1"))
                        .build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(json -> {
                            JSONObject data = JSONObject.parse(json);
                            JSONArray positions = (JSONArray) data.get("positions");

                            Vector<PVector> targets = new Vector<>();
                            for (int j = 0; j < 300; j++) {
                                targets.add(Earth.cartesian(getCoords(positions.getJSONObject(j))));
                            }

                            long unixTimeFirst = positions.getJSONObject(0).getInt("timestamp");
                            sat.refresh(targets, unixTimeFirst);
                        });


            }

        });
    }

    public void draw(PGraphics g) {
        sats.forEach((norad, sat) -> sat.draw(g));
    }

    public static PVector getCoords(JSONObject json) {
        PVector coords = new PVector();
        coords.x = json.getFloat("sataltitude");
        coords.y = json.getFloat("satlatitude");
        coords.z = json.getFloat("satlongitude");

        coords.x *= 0.001; // kilometers to megameters
        coords.y *= FMath.PI / 180.0f; // degrees to radians
        coords.z *= FMath.PI / 180.0f;

        return coords;
    }

}
