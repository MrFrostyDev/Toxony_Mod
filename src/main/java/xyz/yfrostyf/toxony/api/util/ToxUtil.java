package xyz.yfrostyf.toxony.api.util;

public class ToxUtil {

    /**
     * Math formula that returns the results 1, 3, 6, 10... for each x. Formula used to determine mutagen thresholds when tolerance reaches those values. Values are usually multiplied by 100.
     */
    public static float TriangularNumbersMult(float x, float multiplier){
        return( (((x + 1) * (x + 2)) / 2) * multiplier );
    }
}
