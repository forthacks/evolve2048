package com.github.forhacks.evolve2048;

import java.util.List;

public class MaxNeuron extends Neuron {

    private static final double MAX_CHANGE = 0.1;

    public MaxNeuron(List<Integer> parents, Layer prev) {
        super(parents, prev);
    }


    @Override
    public void mutate() {}

    public double[] getData() {

        double value = -1;

        double up = 0;
        double left = 0;
        double down = 0;
        double right = 0;

        for (int i: parents) {

            Neuron parent = prev.neurons.get(i);

            double[] data = parent.getData();

            if (data[0] > value) {
                value = data[0];
                up = data[1];
                left = data[2];
                down = data[3];
                right = data[4];
            }

        }

        return new double[] {value, up, left, down, right};

    }
}