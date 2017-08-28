package com.github.forhacks.evolve2048;

import java.util.List;

public class AndNeuron extends Neuron {

    private static final double MAX_CHANGE = 0.1;

    public AndNeuron(List<Integer> parents, Layer prev) {
        super(parents, prev);
    }

    @Override
    public void mutate() {

        up += Math.random() * MAX_CHANGE;
        left += Math.random() * MAX_CHANGE;
        down += Math.random() * MAX_CHANGE;
        right += Math.random() * MAX_CHANGE;

    }

    public double[] getData() {

        double value = -1;

        double totup = 0;
        double totleft = 0;
        double totdown = 0;
        double totright = 0;

        for (int i: parents) {

            Neuron parent = prev.neurons.get(i);

            double[] data = parent.getData();
            totup += data[1];
            totleft += data[2];
            totdown += data[3];
            totright += data[4];

            if (value == -1 || value == data[0]) {
                value = data[0];
            } else {
                value = 0;
            }

        }

        int size = parents.size();

        return new double[] {value, totup/size, totleft/size, totdown/size, totright/size};

    }

}