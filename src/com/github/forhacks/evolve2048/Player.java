package com.github.forhacks.evolve2048;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Player {
    ArrayList<Layer> layers;
    static int MAX_INIT_LAYER=4;
    static int MIN_INIT_LAYER=2;
    static int MAX_INIT_NODES = 10;
    static int MIN_INIT_NODES = 4;
    int run(int[][] grid){
        Layer start = new Layer(new ArrayList<Node>());
        for (int[] arr:grid) {
            for (int value:arr){
                start.nodes.add(new Node(value));
            }
        }
        layers.get(0).reset();
        layers.add(start);
        for(int i=1; i<layers.size(); i++) {
            layers.get(i).reset();
            if(i%2==0){
                layers.get(i).max(layers.get(i-1));
            }else{
                layers.get(i).and(layers.get(i-1));
            }
        }
        int[] newMovement = new int[4];
        for (Node n:layers.get(layers.size()-1).nodes) {
            for(int i=0; i<4; i++) {
                newMovement[i] += n.movement[i];
            }
        }
        Integer[] indices = {0,1,2,3};
        Arrays.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return newMovement[o1]-newMovement[o2];
            }
        });
        return indices[0];
    }
    static class Layer {
        ArrayList<Node> nodes;
        ArrayList<Connection> connections;
        Layer(ArrayList<Node> nodes) {
            this.nodes = nodes;
        }
        Layer(ArrayList<Node> nodes, ArrayList<Connection> connections) {
            this.nodes = nodes;
            this.connections = connections;
        }
        void reset(){
            int size = nodes.size();
            nodes = new ArrayList<Node>();
            for (int i=0; i<size; i++){
                nodes.add(new Node(0));
            }
        }
        void and(Layer l){
            boolean[] visited = new boolean[l.nodes.size()];
            for (int i = 0; i < connections.size(); i++) {
                int n1 = l.connections.get(i).n1;
                int n2 = l.connections.get(i).n2;
                int toNode = l.connections.get(i).toNode;
                nodes.set(toNode, Node.and(nodes.get(n1), nodes.get(n2)));
            }
        }
        void max(Layer l){
            for (int i = 0; i < connections.size(); i++) {
                int n1 = l.connections.get(i).n1;
                int n2 = l.connections.get(i).n2;
                int toNode = l.connections.get(i).toNode;
                nodes.set(toNode, Node.max(nodes.get(n1), nodes.get(n2)));
            }
        }
        static Layer ranLayer(int prevLayerSize){
            Layer l = new Layer(new ArrayList<>(),new ArrayList<>());
            int nodeNum = (int) (Math.random()*(MAX_INIT_NODES-MIN_INIT_NODES)+MIN_INIT_NODES);
            for (int i=0; i<nodeNum; i++) {
                l.nodes.add(new Node(0));
                int n1 = (int)(Math.random()*prevLayerSize);
                int n2 = (int)(Math.random()*prevLayerSize);
                while(n2==n1){
                    n2 = (int)(Math.random()*prevLayerSize);
                }
                l.connections.add(new Connection(n1, n2, i));
            }
            return l;
        }
    }
    static class Connection{
        int n1, n2, toNode;
        Connection(int n1, int n2, int toNode){
            this.n1 = n1;
            this.n2 = n2;
            this.toNode = toNode;
        }
    }
    static class Node {
        int value;
        float[] movement; // FORMAT: [up, left, down, right]
        Node(int value){
            this.value = value;
            movement = new float[4];
        }
        Node(int value, float[] movement){
            this.value = value;
            this.movement = movement;
        }
        static Node and(Node n1, Node n2){
            float[] newMovement = new float[4];
            if(n1.value != n2.value) {
                return new Node(0, newMovement);
            }
            for (int i = 0; i < 4; i++) {
                newMovement[i] = (n1.movement[i]+n2.movement[i])/2;
            }
            return new Node(n1.value, newMovement);
        }
        static Node max(Node n1, Node n2){
            if (n1.value > n2.value) {
                return n1;
            } else if (n1.value < n2.value) {
                return n2;
            }
            float[] newMovement = new float[4];
            for (int i = 0; i < 4; i++) {
                newMovement[i] = (n1.movement[i]+n2.movement[i])/2;
            }
            return new Node(n1.value, newMovement);
        }
    }
}
