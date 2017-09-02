package com.github.forhacks.evolve2048;

import java.util.ArrayList;

class InputNeuron extends Neuron {

    private double value;

    InputNeuron() {
        super(new ArrayList<>(), null);
    }

    void setValue(int value, int maxvalue) {
        if(value != 0)
            this.value = Math.log(value) / Math.log(maxvalue);
        this._value = this.value;
    }

    public double[] getData() {

        return new double[] {_value, 0, 0, 0, 0};

    }

    public void mutate(Layer prev) {}

}