package com.github.forhacks.evolve2048;

import java.util.List;

abstract class Neuron {

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

    Neuron(List<Integer> parents, Layer prev) {

        this.up = Math.random();
        this.down = Math.random();
        this.left = Math.random();
        this.right = Math.random();

        this.prev = prev;

        this.parents = parents;

    }

    abstract void mutate(Layer prev);

    abstract double[] getData();

}