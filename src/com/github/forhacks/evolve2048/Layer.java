package com.github.forhacks.evolve2048;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Layer {

    private static final double REMOVE_PROB = 0.000001;
    private static final double ADD_PROB = 0.001;
    private static final int MAX_INIT_NODES=10;
    private static final int MIN_INIT_NODES=4;
    private static final double NODE_ADD_PROB = 0.01;
    private static final double NODE_REMOVE_PROB = 0.00001;

    List<Neuron> neurons;

    public void mutate(Layer prev) {

        for (int i = 0; i < neurons.size(); i++) {
            for (int j = 0; j < neurons.get(i).parents.size(); j++) {
                if (Math.random() < REMOVE_PROB) {
                    neurons.get(i).parents.remove(i);
                    j--;
                    continue;
                }
            }
            neurons.get(i).mutate(prev);
        }

        if (Math.random() < NODE_ADD_PROB) {
            addNeuron(prev);
        }

        if (Math.random() < ADD_PROB) {

            int n1 = (int) (Math.random() * prev.neurons.size());
            int n2 = (int) (Math.random() * prev.neurons.size());
            while (n2 == n1) {
                n2 = (int) (Math.random() * prev.neurons.size());
            }

            int n = (int) (Math.random() * this.neurons.size());
            this.neurons.get(n).parents.add(n1);
            this.neurons.get(n).parents.add(n2);

        }

    }

    public Layer(Layer original, Layer prev) {

        neurons = new ArrayList<>();

        for (Neuron n : original.neurons) {
            if (n instanceof AndNeuron)
                neurons.add(new AndNeuron(new ArrayList<>(n.parents), prev));
            else if (n instanceof MaxNeuron)
                neurons.add(new MaxNeuron(new ArrayList<>(n.parents), prev));
            else if (n instanceof InputNeuron)
                neurons.add(new InputNeuron());
        }

    }

    // Input Neurons
    public Layer() {

        neurons = new ArrayList<>();

        for (int i = 0; i < 16; i++)
            neurons.add(new InputNeuron());

    }

    // [and, max]
    public Layer(Layer prev) {

        neurons = new ArrayList<>();

        int size = (int) (Math.random() * (MAX_INIT_NODES - MIN_INIT_NODES) + MIN_INIT_NODES);

        for (int i = 0; i < size; i++) {
            addNeuron(prev);
        }

    }

    public void addNeuron(Layer prev) {

        int n1 = (int) (Math.random() * prev.neurons.size());
        int n2 = (int) (Math.random() * prev.neurons.size());

        while (n2 == n1) {
            n2 = (int) (Math.random() * prev.neurons.size());
        }

        int type = (int) (Math.random() * 3);

        switch (type){
            case 0:
                this.neurons.add(
                    new AndNeuron(
                            new ArrayList<>(Arrays.asList(n1, n2)), prev
                    )
                );
                break;
            case 1:
                this.neurons.add(
                        new MaxNeuron(
                                new ArrayList<>(Arrays.asList(n1, n2)), prev
                        )
                );
                break;
            case 2:
                this.neurons.add(
                        new WieghtNeuron(
                                new ArrayList<>(Arrays.asList(n1)), prev
                        )
                );
                break;
        }
    }

}