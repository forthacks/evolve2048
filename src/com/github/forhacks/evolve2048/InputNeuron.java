package com.github.forhacks.evolve2048;

import java.util.ArrayList;

public class InputNeuron extends Neuron {

    private static final double MAX_CHANGE = 0.1;

    double value;

    public InputNeuron() {
        super(new ArrayList<>(), null);
        up = 0;
        left = 0;
        down = 0;
        right = 0;
    }

    public double[] getData() {

        return new double[] {value, up, left, down, right};

    }

    public void mutate() {}

}