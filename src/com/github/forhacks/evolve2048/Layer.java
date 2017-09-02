package com.github.forhacks.evolve2048;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Layer {

    private static final int MAX_INIT_NODES=10;
    private static final int MIN_INIT_NODES=4;
    private static final double NODE_ADD_PROB = 0.1;

    List<Neuron> neurons;

    void mutate(Layer prev) {

        for (Neuron n : neurons) {
            n.mutate(prev);
        }

        if (Main.random.nextDouble() < NODE_ADD_PROB) {
            addNeuron(prev);
        }

    }

    Layer(Layer original, Layer prev) {

        neurons = new ArrayList<>();

        for (Neuron n : original.neurons) {
            if (n instanceof AndNeuron)
                neurons.add(new AndNeuron(new ArrayList<>(n.parents), prev, n.up, n.left, n.down, n.right));
            else if (n instanceof MinNeuron)
                neurons.add(new MinNeuron(new ArrayList<>(n.parents), prev));
            else if (n instanceof InputNeuron)
                neurons.add(new InputNeuron());
        }

    }

    // Input Neurons
    Layer() {

        neurons = new ArrayList<>();

        for (int i = 0; i < 16; i++)
            neurons.add(new InputNeuron());

    }

    // [and, max]
    Layer(Layer prev) {

        neurons = new ArrayList<>();

        int size = Main.random.nextInt(MAX_INIT_NODES - MIN_INIT_NODES) + MIN_INIT_NODES;

        for (int i = 0; i < size; i++) {
            addNeuron(prev);
        }

    }

    private void addNeuron(Layer prev) {

        int n1 = Main.random.nextInt(prev.neurons.size());
        int n2 = Main.random.nextInt(prev.neurons.size());

        while (n2 == n1) {
            n2 = Main.random.nextInt(prev.neurons.size());
        }

        int type = Main.random.nextInt(2);

        if (type == 0)
            this.neurons.add(
                new AndNeuron(
                        new ArrayList<>(Arrays.asList(n1, n2)), prev
                )
            );
        else if (type == 1)
            this.neurons.add(
                    new MinNeuron(
                            new ArrayList<>(Arrays.asList(n1, n2)), prev
                    )
            );
    }

}