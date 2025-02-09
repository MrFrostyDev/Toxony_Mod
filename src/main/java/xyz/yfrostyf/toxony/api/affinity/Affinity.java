package xyz.yfrostyf.toxony.api.affinity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Affinity {
    public static final List<Integer> AFFINITY_LIST = Arrays.asList(Affinity.WOLF, Affinity.CAT, Affinity.TURTLE, Affinity.SPIDER);

    public static final int NONE = 0;
    public static final int WOLF = 1;
    public static final int CAT = 2;
    public static final int TURTLE = 3;
    public static final int SPIDER = 4;

    public static HashMap<Integer, Integer> initAffinities(){
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int affinity : AFFINITY_LIST){
            map.put(affinity, 0);
        }
        return map;
    }
}

