package st;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import st.util.FMath;

import java.util.List;

public class Satellite {

    private static PShape shape;
    private static Object shapeLock = new Object();

    private PVector pos, vel;

    public Satellite(PGraphics g, PVector coord1, PVector coord2, long t1, long t2) {
        refresh(coord1, coord2, t1, t2);


        synchronized (shapeLock){
            if(shape == null) {
                shape = g.createShape(PShape.POINT, 0.0f, 0.0f, 0.0f);
                shape.setStroke(0xFFFF0000);
                shape.scale(3.0f);
            }
        }


    }

    public void update(float dt) {
        // a = F * 1/m_1
        // F = G * (m_1*m_2)/(r^2)
        // a = G * m_2 / r^2

        float accMag =  FMath.G * Earth.MASS / pos.magSq();
        PVector acc = pos.copy().normalize().mult(-accMag);

        vel.add(acc.mult(dt));
        pos.add(vel.copy().mult(dt));
    }

    public void draw(PGraphics g) {
        g.pushMatrix();
        g.translate(pos.x, pos.y, pos.z);

        g.shape(shape);

        g.popMatrix();
    }

    public void refresh(PVector coord1, PVector coord2, long t1, long t2) {
        coord1.x *= 0.001;
        coord2.x *= 0.001;

        float deltat = (float)(t2 - t1);

        coord2.z -= deltat * 0.00007272205f;

        coord1.y *= FMath.PI / 180.0f;
        coord1.z *= FMath.PI / 180.0f;

        coord2.y *= FMath.PI / 180.0f;
        coord2.z *= FMath.PI / 180.0f;

        pos = Earth.cartesian(coord1);
        PVector pos2 = Earth.cartesian(coord2);

        vel = PVector.sub(pos2, pos);
    }


}
