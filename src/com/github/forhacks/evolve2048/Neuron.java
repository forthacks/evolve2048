package com.github.forhacks.evolve2048;

import java.util.List;

public abstract class Neuron {

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

    abstract public double[] getData();
    abstract void mutate();

}