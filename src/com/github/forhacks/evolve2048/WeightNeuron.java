package com.github.forhacks.evolve2048;

import java.util.List;

public class WeightNeuron extends Neuron {

    private static final double MAX_INIT_WEIGHT = 2;

    public double weight;

    public WeightNeuron(List<Integer> parents, Layer prev) {
        super(parents, prev);
        weight = Math.random() * MAX_INIT_WEIGHT;
    }

    @Override
    public void mutate(Layer prev) {
        int n = (int) (Math.random() * prev.neurons.size());
        parents.add(n);

        weight += Math.random() * 2 * MAX_CHANGE - MAX_CHANGE;
    }

    @Override
    public double[] getData() {
        int parent = parents.get(parents.size()-1);
        double[] parentData = prev.neurons.get(parent).getData();
        double value = parentData[0]* weight;
        double[] data = new double[]{value, parentData[1], parentData[2], parentData[3], parentData[4]};
        return data;
    }
}
