package xyz.yfrostyf.toxony.api.items;

import xyz.yfrostyf.toxony.api.affinity.Affinity;

import java.util.List;

public interface AffinityIngredient {
    List<Affinity> getPossibleAffinities();
}


