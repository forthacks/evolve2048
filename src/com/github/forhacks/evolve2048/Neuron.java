package com.github.forhacks.evolve2048;

import java.util.ArrayList;
import java.util.List;

public abstract class Neuron {

    private static final double MAX_CHANGE = 0.1;

    double up;
    double left;
    double down;
    double right;

    List<Integer> parents;
    Layer prev;

    public Neuron(List<Integer> parents, Layer prev) {

        this.up = Math.random();
        this.down = Math.random();
        this.left = Math.random();
        this.right = Math.random();

        this.prev = prev;

        this.parents = parents;

    }

    public void mutate() {

        up += Math.random() * MAX_CHANGE;
        left += Math.random() * MAX_CHANGE;
        down += Math.random() * MAX_CHANGE;
        right += Math.random() * MAX_CHANGE;

    }

    abstract public double[] getData();

}