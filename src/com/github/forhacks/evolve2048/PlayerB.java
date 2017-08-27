package com.github.forhacks.evolve2048;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PlayerB {

    private static final int MAX_INIT_LAYER=4;
    private static final int MIN_INIT_LAYER=2;
    private static final int MAX_INIT_NODES=10;
    private static final int MIN_INIT_NODES=4;
    private static final double MAX_CHANGE = 0.1;
    private static final double REMOVE_PROB = 0.000001;
    private static final double ADD_PROB = 0.001;
    private static final double NODE_ADD_PROB = 0.01;
    private static final double NODE_REMOVE_PROB = 0.00001;
    private static final double LAYER_ADD_PROB = 0.001;

    List<Layer> layers;

    public PlayerB() {

        int size = (int) (Math.random() * (MAX_INIT_LAYER - MIN_INIT_LAYER) + MIN_INIT_LAYER);

        layers = new ArrayList<>();

        layers.add(new Layer());

        for (int i = 1; i < size; i++) {

            layers.add(new Layer(layers.get(i - 1)));

        }

    }

    public PlayerB(PlayerB original) {

        layers = new ArrayList<>();

        layers.add(new Layer());

        for (int i = 1; i < original.layers.size(); i++) {
            layers.add(new Layer(original.layers.get(i), layers.get(i - 1)));
        }

    }

    int run(Game game) {

        double[] move = new double[4];

        for (int i = 0 ; i < layers.get(layers.size() - 1).neurons.size(); i++) {

            double[] data = layers.get(layers.size() - 1).neurons.get(i).getData();

            move[0] += data[1];
            move[1] += data[2];
            move[2] += data[3];
            move[3] += data[4];

        }

        Integer[] indices = new Integer[] {0, 1, 2, 3};

        Arrays.sort(indices, Comparator.comparingInt((Integer o) -> (int) move[o]));

        int i = 0;
        while (!game.canMove(indices[i])) {
            i++;
        }

        return indices[i];

    }

    public void mutate() {


        for (int i = 1; i < layers.size(); i++) {
            layers.get(i).mutate(layers.get(i - 1));
        }

        if (Math.random() < LAYER_ADD_PROB) {
            layers.add(new Layer(layers.get(layers.size() - 1)));
        }

    }

    private abstract class Neuron {

        double up;
        double left;
        double down;
        double right;

        List<Integer> parents;
        Layer prev;

        private Neuron(List<Integer> parents, Layer prev) {

            this.up = Math.random();
            this.down = Math.random();
            this.left = Math.random();
            this.right = Math.random();

            this.prev = prev;

            this.parents = parents;

        }

        private Neuron(Neuron original) {

            this.up = original.up;
            this.left = original.left;
            this.down = original.down;
            this.right = original.right;

            this.parents = new ArrayList<>();

        }

        public void mutate() {

            up += Math.random() * MAX_CHANGE;
            left += Math.random() * MAX_CHANGE;
            down += Math.random() * MAX_CHANGE;
            right += Math.random() * MAX_CHANGE;

        }

        abstract public double[] getData();

    }

    private class AndNeuron extends Neuron {

        public AndNeuron(List<Integer> parents, Layer prev) {
            super(parents, prev);
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

    private class MaxNeuron extends Neuron {

        public MaxNeuron(List<Integer> parents, Layer prev) {
            super(parents, prev);
        }

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

    private class InputNeuron extends Neuron {

        double value;

        public InputNeuron() {
            super(new ArrayList<>(), null);
        }

        public double[] getData() {

            return new double[] {value, up, left, down, right};

        }

    }

    private class Layer {

        List<Neuron> neurons;

        public void mutate(Layer prev) {

            for (int i = 0; i < neurons.size(); i++) {
                for (int j = 0; j < neurons.get(i).parents.size(); j++) {
                    if (Math.random() < REMOVE_PROB) {
                        neurons.get(i).parents.remove(i);
                        j--;
                        continue;
                    }
                    neurons.get(i).mutate();
                }
            }

            if (Math.random() < NODE_ADD_PROB) {
                addNeuron(prev);
            }

            if (Math.random() < ADD_PROB) {

                int n = (int) (Math.random() * prev.neurons.size());
                this.neurons.get((int) (Math.random() * this.neurons.size())).parents.add(n);

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

            int type = (int) (Math.random() * 2);

            this.neurons.add(
                    type == 0 ? new AndNeuron(
                            new ArrayList<>(Arrays.asList(n1, n2)), prev
                    ) : new MaxNeuron(
                            new ArrayList<>(Arrays.asList(n1, n2)), prev
                    )
            );
        }

    }

}
