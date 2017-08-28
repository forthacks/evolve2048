package com.github.forhacks.evolve2048;

import java.util.ArrayList;

public class InputNeuron extends Neuron {

    double value;

    public InputNeuron() {
        super(new ArrayList<>(), null);
    }

    public double[] getData() {

        return new double[] {value, up, left, down, right};

    }

}