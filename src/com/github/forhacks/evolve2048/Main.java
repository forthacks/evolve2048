package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class Main extends JPanel {

    public static HeaderPanel header;
    public static GamePanel game;
    public static GraphPanel graph;
    public static NetworkPanel network;
    public static Evolution evolution;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        JFrame frame = new JFrame();
        frame.setTitle("2048");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setSize(1000, 800);
        frame.setResizable(false);

        header = new HeaderPanel();
        game = new GamePanel(false);
        graph = new GraphPanel();
        network = new NetworkPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints headerc = new GridBagConstraints();
        headerc.fill = GridBagConstraints.HORIZONTAL;
        headerc.gridx = 0;
        headerc.gridy = 0;
        headerc.gridwidth = 2;
        headerc.gridheight = 1;
        headerc.weightx = 1;

        GridBagConstraints gamec = new GridBagConstraints();
        gamec.fill = GridBagConstraints.HORIZONTAL;
        gamec.gridx = 0;
        gamec.gridy = 1;
        gamec.gridwidth = 1;
        gamec.weightx = 1;

        GridBagConstraints graphc = new GridBagConstraints();
        graphc.fill = GridBagConstraints.HORIZONTAL;
        graphc.gridx = 1;
        graphc.gridy = 1;
        graphc.gridwidth = 1;
        graphc.weightx = 1;

        GridBagConstraints networkc = new GridBagConstraints();
        networkc.fill = GridBagConstraints.HORIZONTAL;
        networkc.gridx = 0;
        networkc.gridy = 2;
        networkc.gridwidth = 2;
        networkc.weightx = 1;

        panel.add(header, headerc);
        panel.add(game, gamec);
        panel.add(graph, graphc);
        panel.add(network, networkc);

        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        evolution = new Evolution();
        evolution.start();

    }

}
