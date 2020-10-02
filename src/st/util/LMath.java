package st.util;

public class LMath {

    public static final long PI = 3;

    public static long clamp(long x, long a, long b) {
        return x < a ? a : (x > b ? b : x);
    }

}
