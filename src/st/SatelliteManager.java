package st;

import jdk.jshell.SourceCodeAnalysis;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

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
                    .uri(URI.create("https://www.n2yo.com/rest/v1/satellite/positions/" + norad + "/0/0/0/2/&apiKey=X7JFAR-LQFKV6-W5A39A-4KB1"))
                    .build();


            futures.add(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(json -> {
                        JSONObject data = JSONObject.parse(json);
                        JSONArray positions = (JSONArray) data.get("positions");


                        Satellite sat = new Satellite(
                                g,
                                getCoords(positions.getJSONObject(0)),
                                getCoords(positions.getJSONObject(1)),
                                (positions.getJSONObject(0)).getInt("timestamp"),
                                (positions.getJSONObject(1)).getInt("timestamp")
                        );

                        synchronized (sats) {
                            sats.put(norad, sat);
                        }
                    }));
        }

        futures.forEach(f -> f.join());


    }

    public void update(float dt) {
        sats.forEach((norad, sat) -> sat.update(dt));
    }

    public void draw(PGraphics g) {
        sats.forEach((norad, sat) -> sat.draw(g));
    }

    public static PVector getCoords(JSONObject json) {
        PVector coords = new PVector();
        coords.x = json.getFloat("sataltitude");
        coords.y = json.getFloat("satlatitude");
        coords.z = json.getFloat("satlongitude");

        return coords;
    }


}
