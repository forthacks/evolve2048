package com.github.forhacks.evolve2048;

import java.util.List;

public class WieghtNeuron extends Neuron{

    static final double MAX_INIT_WIEGHT = 2;

    public double wieght;

    public WieghtNeuron(List<Integer> parents, Layer prev) {
        super(parents, prev);
        wieght = Math.random()*MAX_INIT_WIEGHT;
    }

    @Override
    public void mutate(Layer prev) {
        int n = (int) (Math.random() * prev.neurons.size());
        parents.add(n);

        wieght += Math.random() * 2 * MAX_CHANGE - MAX_CHANGE;
    }

    @Override
    public double[] getData() {
        int parent = parents.get(parents.size()-1);
        double[] parentData = prev.neurons.get(parent).getData();
        double value = parentData[0]*wieght;
        double[] data = new double[]{value, parentData[1], parentData[2], parentData[3], parentData[4]};
        return data;
    }
}
