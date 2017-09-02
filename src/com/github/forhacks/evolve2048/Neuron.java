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

        this.up = Main.random.nextDouble();
        this.down = Main.random.nextDouble();
        this.left = Main.random.nextDouble();
        this.right = Main.random.nextDouble();

        this.prev = prev;

        this.parents = parents;

    }

    abstract void mutate(Layer prev);

    abstract double[] getData();

}