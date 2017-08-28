package com.github.forhacks.evolve2048;

import java.util.ArrayList;
import java.util.List;

public abstract class Neuron {

    public static final double MAX_CHANGE = 0.1;

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

    public Neuron(Neuron original) {

        this.up = original.up;
        this.left = original.left;
        this.down = original.down;
        this.right = original.right;

        this.parents = new ArrayList<>();

    }

    abstract public void mutate();

    abstract public double[] getData();

}