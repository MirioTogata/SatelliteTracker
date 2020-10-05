package st;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import st.util.FMath;
import st.util.LMath;
import st.util.Ray;

import java.util.List;
import java.util.Vector;

public class Satellite {

    private static PShape shape;
    private static Object shapeLock = new Object();

    private PVector pos;

    private List<PVector> targets;
    private long tfirst;

    public Satellite(PGraphics g, List<PVector> targets, long tfirst) {
        refresh(targets, tfirst);

        synchronized (shapeLock) {
            if (shape == null) {
                shape = g.createShape(PShape.POINT, 0.0f, 0.0f, 0.0f);
                shape.setStroke(0xFFFF0000);
                shape.scale(3.0f);
            }
        }
    }

    public boolean update(long unixTime, long unixTimeMilli) {

        long targetIndex = unixTime - tfirst + 1;
        boolean needsRefresh = targetIndex >= 290;

        targetIndex = LMath.clamp(targetIndex, 1, 299);

        pos = PVector.lerp(targets.get((int)targetIndex - 1), targets.get((int)targetIndex), (unixTimeMilli - unixTime * 1000) / 1000.0f);

        return needsRefresh;
    }

    public void draw(PGraphics g) {
        g.pushMatrix();
        g.translate(pos.x, pos.y, pos.z);

        g.shape(shape);

        g.popMatrix();
    }

    public void refresh(List<PVector> targets, long tfirst) {
        this.targets = targets;
        this.tfirst = tfirst;
    }

    public float intersect(Ray ray) {
        return 0.0f;
    }

}
