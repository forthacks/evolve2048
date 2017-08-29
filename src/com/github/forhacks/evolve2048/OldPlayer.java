package com.github.forhacks.evolve2048;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class OldPlayer implements Cloneable {

    public ArrayList<Layer> layers;

    private static final int MAX_INIT_LAYER=4;
    private static final int MIN_INIT_LAYER=2;
    private static final int MAX_INIT_NODES = 5;
    private static final int MIN_INIT_NODES = 3;
    private static final double MAX_CHANGE = 0.1;
    private static final double REMOVE_PROB = 0.00005;
    private static final double CONNECTION_ADD_PROB = 0.05;
    private static final double NODE_ADD_PROB = 0.05;
    private static final double LAYER_ADD_PROB = 0.001;

    private OldPlayer(ArrayList<Layer> l) {
        layers = l;
    }

    @Override
    public OldPlayer clone(){
        ArrayList<Layer> newLayer = new ArrayList<>();
        for (Layer i : layers) {
            newLayer.add(i.c());
        }
        return new OldPlayer(newLayer);
    }

    void mutate() {
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).mutate((i > 0) ? layers.get(i - 1).nodes.size() : 16);
        }

        if (chance(LAYER_ADD_PROB)){
            layers.add(Layer.generate(layers.get(layers.size() - 1).nodes.size()));
        }
    }

    static OldPlayer generate() {

        int layerNum = (int) (Math.random() * (MAX_INIT_LAYER - MIN_INIT_LAYER) + MIN_INIT_LAYER);

        OldPlayer p = new OldPlayer(new ArrayList<>());

        for(int i = 0; i < layerNum; i++) {
            p.layers.add(Layer.generate((i > 0) ? p.layers.get(i - 1).nodes.size() : 16));
        }
        return p;
    }

    int run(Game game) {
        Layer start = new Layer(new ArrayList<>());

        int[][] grid = game.grid;

        for (int[] arr : grid) {
            for (int value : arr){
                start.nodes.add(new Node(value));
            }
        }

        layers.get(0).reset();
        layers.add(start);

        for (int i = 1; i < layers.size(); i++) {
            layers.get(i).reset();
            if (i % 2 == 0) {
                layers.get(i - 1).max(layers.get(i));
            } else {
                layers.get(i - 1).and(layers.get(i));
            }
        }

        int[] newMovement = new int[4];

        for (Node n : layers.get(layers.size() - 1).nodes) {
            for (int i = 0; i < 4; i++) {
                newMovement[i] += n.movement[i];
            }
        }

        Integer[] indices = new Integer[] {0, 1, 2, 3};

        Arrays.sort(indices, Comparator.comparingInt((Integer o) -> newMovement[o]));
        int i = 0;
        while (!game.canMove(indices[i])) i++;
        return indices[i];
    }

    static void shuffleArray(Integer[] ar) {

        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }

    }

    static class Layer {

        ArrayList<Node> nodes;
        ArrayList<Connection> connections;

        Layer(ArrayList<Node> nodes) {
            this.nodes = nodes;
            connections = new ArrayList<>();
        }

        Layer(ArrayList<Node> nodes, ArrayList<Connection> connections) {
            this.nodes = nodes;
            this.connections = connections;
        }

        void reset() {

            int size = nodes.size();
            nodes = new ArrayList<>();

            for (int i = 0; i < size; i++){
                nodes.add(new Node(0));
            }

        }

        void and(Layer l) {

            for (int i = 0; i < l.connections.size(); i++) {
                int n1 = l.connections.get(i).n1;
                int n2 = l.connections.get(i).n2;
                int toNode = l.connections.get(i).toNode;
                double[] addMove = l.connections.get(i).newMove;
                l.nodes.set(toNode, Node.and(nodes.get(n1), nodes.get(n2), addMove));
            }

        }
        void max(Layer l) {

            for (int i = 0; i < l.connections.size(); i++) {
                int n1 = l.connections.get(i).n1;
                int n2 = l.connections.get(i).n2;
                int toNode = l.connections.get(i).toNode;
                l.nodes.set(toNode, Node.max(nodes.get(n1), nodes.get(n2)));
            }

        }

        void mutate(int prevLayerSize) {

            for (int i = 0; i < connections.size(); i++){
                connections.get(i).clone().mutate();
                if (chance(REMOVE_PROB)) {
                    connections.remove(i);
                    i--;
                }
            }

            if (chance(CONNECTION_ADD_PROB)) {
                int n1 = (int) (Math.random() * prevLayerSize);
                int n2 = (int) (Math.random() * prevLayerSize);
                while (n2 == n1) {
                    n2 = (int) (Math.random() * prevLayerSize);
                }
                connections.add(new Connection(n1, n2, (int) (Math.random() * nodes.size())));
            }

            if (chance(NODE_ADD_PROB)) {
                int n1 = (int) (Math.random() * prevLayerSize);
                int n2 = (int) (Math.random() * prevLayerSize);
                while (n2 == n1) {
                    n2 = (int) (Math.random() * prevLayerSize);
                }
                nodes.add(new Node(0));
                connections.add(new Connection(n1, n2, nodes.size()-1));
            }
        }

        static Layer generate(int prevLayerSize) {

            Layer l = new Layer(new ArrayList<>(), new ArrayList<>());
            int nodeNum = (int) (Math.random() * (MAX_INIT_NODES - MIN_INIT_NODES) + MIN_INIT_NODES);

            for (int i = 0; i < nodeNum; i++) {

                l.nodes.add(new Node(0));
                int n1 = (int)(Math.random()*prevLayerSize);
                int n2 = (int)(Math.random()*prevLayerSize);

                while(n2 == n1){
                    n2 = (int)(Math.random()*prevLayerSize);
                }

                l.connections.add(new Connection(n1, n2, i));

            }

            return l;

        }
        Layer c() {
            ArrayList<Connection> newc = new ArrayList<>();
            for (Connection c : newc) {
                newc.add(c.clone());
            }
            return new Layer(nodes, newc);
        }
    }
    static boolean chance(Double d) {
        return Math.random() < d;
    }

    static class Connection {

        int n1, n2, toNode;
        double[] newMove = new double[4];

        Connection(int n1, int n2, int toNode) {
            this.n1 = n1;
            this.n2 = n2;
            this.toNode = toNode;
            for (int i = 0; i < 4; i++) {
                newMove[i] = Math.random();
            }
        }
        Connection(int n1, int n2, int toNode, double[] newMove) {
            this.n1 = n1;
            this.n2 = n2;
            this.toNode = toNode;
            this.newMove = newMove;
        }

        void mutate() {
            for (int i = 0; i < 4; i++) {
                newMove[i] += Math.random()*2*MAX_CHANGE-MAX_CHANGE;
            }
        }

        public Connection clone(){
            return new Connection(n1, n2, toNode, newMove);
        }
    }

    static class Node {

        int value;
        double[] movement; // FORMAT: [up, left, down, right]

        Node(int value) {
            this.value = value;
            movement = new double[4];
        }

        Node(int value, double[] movement) {
            this.value = value;
            this.movement = movement;
        }

        static Node and(Node n1, Node n2, double[] addMove) {
            double[] newMovement = new double[4];

            if (n1.value != n2.value) {
                return new Node(0, newMovement);
            }

            for (int i = 0; i < 4; i++) {
                newMovement[i] = (n1.movement[i]+n2.movement[i])/2 + addMove[i];
            }

            return new Node(n1.value, newMovement);
        }

        static Node max(Node n1, Node n2){
            if (n1.value > n2.value) {
                return n1;
            } else if (n1.value < n2.value) {
                return n2;
            }

            double[] newMovement = new double[4];

            for (int i = 0; i < 4; i++) {
                newMovement[i] = (n1.movement[i]+n2.movement[i])/2;
            }

            return new Node(n1.value, newMovement);
        }

    }

}
