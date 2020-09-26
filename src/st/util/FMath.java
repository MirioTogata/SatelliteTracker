package st.util;

import java.lang.Math;

public class FMath {

    public static final float PI = (float)Math.PI;

    public static float sin(float x) {
        return (float)Math.sin(x);
    }

    public static float cos(float x) {
        return (float)Math.cos(x);
    }

    public static float min(float a, float b){
        return a < b ? a : b;
    }

    public static float max(float a, float b){
        return a < b ? b : a;
    }

    public static float clamp(float x, float a, float b){
        return x < a ? a : (x > b ? b : x);
    }

}
