package st;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.PGraphics3D;
import st.util.FMath;
import st.util.LMath;
import st.util.Ray;

import java.util.List;
import java.util.Vector;

public class Satellite {

    private static PShape shape;
    public static void init(PGraphics g) {
        if (shape == null) {
            shape = g.createShape(PShape.POINT, 0.0f, 0.0f, 0.0f);
            shape.setStroke(0xFFFF0000);
            shape.scale(3.0f);
        }
    }

    private PVector pos;

    private List<PVector> targets;
    private long tfirst;

    public Satellite(List<PVector> targets, long tfirst) {
        refresh(targets, tfirst);
    }

    public boolean update(long unixTime, long unixTimeMilli) {

        long targetIndex = unixTime - tfirst + 1;
        boolean needsRefresh = targetIndex >= 290;

        targetIndex = LMath.clamp(targetIndex, 1, 299);

        pos = PVector.lerp(targets.get((int)targetIndex - 1), targets.get((int)targetIndex), (unixTimeMilli - unixTime * 1000) / 1000.0f);

        return needsRefresh;
    }

    public void draw(PGraphics g) {
        if(pos == null) return;

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
        PVector ori_ = PVector.sub(ray.ori, pos);

        float a = PVector.dot(ray.dir, ray.dir);
        float b = 2 * PVector.dot(ray.dir, ori_);
        float c = PVector.dot(ori_, ori_) - 0.01f;

        float d = b*b - 4 * a * c;

        if(d < 0.0f){
            return Float.POSITIVE_INFINITY;
        }

        float t1 = (-b + FMath.sqrt(d))/(2.0f * a);
        float t2 = (-b - FMath.sqrt(d))/(2.0f * a);

        return FMath.min(t1,t2);
    }

    public PVector getPos() {
        return pos;
    }

}
