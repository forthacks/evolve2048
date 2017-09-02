package com.github.forhacks.evolve2048;

import java.util.List;

class AndNeuron extends Neuron {

    private static final double MAX_CHANGE = 0.1;
    private static final double ADD_PROB = 0.05;

    AndNeuron(List<Integer> parents, Layer prev) {
        super(parents, prev);
    }

    AndNeuron(List<Integer> parents, Layer prev, double up, double left, double down, double right) {
        super(parents, prev);
        this.up = up;
        this.left = left;
        this.down = down;
        this.right = right;
    }

    @Override
    void mutate(Layer prev) {

        up += Main.random.nextDouble() * 2 * MAX_CHANGE - MAX_CHANGE;
        left += Main.random.nextDouble() * 2 * MAX_CHANGE - MAX_CHANGE;
        down += Main.random.nextDouble() * 2 * MAX_CHANGE - MAX_CHANGE;
        right += Main.random.nextDouble() * 2 * MAX_CHANGE - MAX_CHANGE;

        if (Main.random.nextDouble() < ADD_PROB) {

            int n1 = Main.random.nextInt(prev.neurons.size());
            int n2 = Main.random.nextInt(prev.neurons.size());
            while (n2 == n1) {
                n2 = Main.random.nextInt(prev.neurons.size());
            }

            parents.add(n1);
            parents.add(n2);

        }
    }

    public double[] getData() {

        _up = up; _left = left; _down = down; _right = right;

        int i = parents.size();

        Neuron parent1 = prev.neurons.get(parents.get(i - 2));
        Neuron parent2 = prev.neurons.get(parents.get(i - 1));

        double[] data1 = parent1.getData();
        double[] data2 = parent2.getData();

        _value = Math.abs(data1[0] - data2[0]);

        _up += (data1[1] + data2[1])/2;
        _left += (data1[2] + data2[2])/2;
        _down += (data1[3] + data2[3])/2;
        _right += (data1[4] + data2[4])/2;

        return new double[] {_value, _up, _left, _down, _right};

    }

}