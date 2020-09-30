package st;

import processing.core.PShape;
import processing.core.PVector;
import st.util.FMath;

import java.util.List;

public class Satellite {

    private static PShape shape;

    private PVector pos;
    private PVector vel;

    public Satellite(
            PVector coord1, PVector coord2) {
        pos = Earth.cartesian(coord1.x, coord1.y, coord1.z);
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




}
