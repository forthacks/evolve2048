package com.github.forhacks.evolve2048;

import java.util.ArrayList;
import java.util.List;

public abstract class Neuron {

    double up;
    double left;
    double down;
    double right;

    // Most recent values returned
    double _value;
    double _up;
    double _left;
    double _down;
    double _right;

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

    abstract public void mutate(Layer prev);

    abstract public double[] getData();

}