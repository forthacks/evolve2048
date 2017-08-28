package com.github.forhacks.evolve2048;

import java.util.ArrayList;

public class InputNeuron extends Neuron {

    private static final double MAX_CHANGE = 0.1;

    double value;

    public InputNeuron() {
        super(new ArrayList<>(), null);
    }

    public double[] getData() {

        return new double[] {value, up, left, down, right};

    }

    public void mutate() {

        up += Math.random() * MAX_CHANGE;
        left += Math.random() * MAX_CHANGE;
        down += Math.random() * MAX_CHANGE;
        right += Math.random() * MAX_CHANGE;

    }

}