package com.github.forhacks.evolve2048;

import java.util.List;

public class WeightNeuron extends Neuron {

    private static final double MAX_INIT_WEIGHT = 2;
    private static final double MAX_CHANGE = 0.1;
    private static final double ADD_PROB = 0.05;

    public double weight;

    public WeightNeuron(List<Integer> parents, Layer prev) {
        super(parents, prev);
        weight = Math.random() * MAX_INIT_WEIGHT;
    }

    @Override
    public void mutate(Layer prev) {

        weight += Math.random() * 2 * MAX_CHANGE - MAX_CHANGE;

        if (Math.random() < ADD_PROB) {

            int n = (int) (Math.random() * prev.neurons.size());
            parents.add(n);

        }

    }

    @Override
    public double[] getData() {

        int parent = parents.get(parents.size()-1);
        double[] data = prev.neurons.get(parent).getData();

        double value = data[0] * weight;
        double[] result = new double[] {value, data[1], data[2], data[3], data[4]};
        return result;

    }
}
