package st.rendering;

import processing.core.PApplet;
import processing.core.PConstants;

public class Window extends PApplet {

    @Override
    public void settings() {
        size(1280, 720, PConstants.P3D);
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        background(0.0f);
    }
}
