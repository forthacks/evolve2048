package com.github.forhacks.evolve2048;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Layer {

    private static final int MAX_INIT_NODES=10;
    private static final int MIN_INIT_NODES=4;
    private static final double NODE_ADD_PROB = 0.1;

    // 0: and
    // 1: min
    // 3: ipt
    private int type;

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

        type = original.type;

        for (Neuron n : original.neurons) {
            if (type == 0)
                neurons.add(new AndNeuron(new ArrayList<>(n.parents), prev, n.up, n.left, n.down, n.right));
            else if (type == 1)
                neurons.add(new MinNeuron(new ArrayList<>(n.parents), prev));
            else if (type == 2)
                neurons.add(new InputNeuron());
        }

    }

    // Input Neurons
    Layer() {

        neurons = new ArrayList<>();
        type = 3;

        for (int i = 0; i < 16; i++)
            neurons.add(new InputNeuron());

    }

    // [and, max]
    Layer(Layer prev) {

        type = (prev.type + 1) % 2;

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