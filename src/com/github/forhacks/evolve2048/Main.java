package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;

public class Main extends JPanel {

    static GamePanel game;
    static GraphPanel graph;
    static NetworkPanel network;
    static Evolution evolution;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        JFrame frame = new JFrame();
        frame.setTitle("2048");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setSize(1000, 800);
        frame.setResizable(false);

        game = new GamePanel(true);
        graph = new GraphPanel();
        network = new NetworkPanel();
        evolution = new Evolution();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints one = new GridBagConstraints();
        one.fill = GridBagConstraints.HORIZONTAL;
        one.gridx = 0;
        one.gridy = 0;
        one.ipady = 20;
        one.gridwidth = 2;
        one.weightx = 1;

        GridBagConstraints two = new GridBagConstraints();
        two.fill = GridBagConstraints.HORIZONTAL;
        two.gridx = 2;
        two.gridy = 0;
        two.ipady = 20;
        two.gridwidth = 2;
        two.weightx = 1;

        GridBagConstraints three = new GridBagConstraints();
        three.fill = GridBagConstraints.HORIZONTAL;
        three.gridx = 4;
        three.gridy = 0;
        three.ipady = 20;
        three.gridwidth = 2;
        three.weightx = 1;

        JButton start = new JButton("Start");
        start.addActionListener((ActionEvent e) -> {
            try {
                evolution.start();
            } catch (Exception err) {
                System.out.println("Error!");
            }
        });
        JButton pause = new JButton("Pause");
        pause.addActionListener((ActionEvent e) -> {
            try {
                evolution.pause();
            } catch (Exception err) {
                System.out.println("Error!");
            }
        });
        JButton run = new JButton("Analyze");
        run.addActionListener((ActionEvent e) -> {
            try {
                evolution.runBest();
            } catch (Exception err) {
                System.out.println("Error!");
            }
        });

        panel.add(start, one);
        panel.add(pause, two);
        panel.add(run, three);

        GridBagConstraints gamec = new GridBagConstraints();
        gamec.fill = GridBagConstraints.HORIZONTAL;
        gamec.gridx = 0;
        gamec.gridy = 1;
        gamec.gridwidth = 3;
        gamec.weightx = 1;

        GridBagConstraints graphc = new GridBagConstraints();
        graphc.fill = GridBagConstraints.HORIZONTAL;
        graphc.gridx = 3;
        graphc.gridy = 1;
        graphc.gridwidth = 3;
        graphc.weightx = 1;

        GridBagConstraints networkc = new GridBagConstraints();
        networkc.fill = GridBagConstraints.HORIZONTAL;
        networkc.gridx = 0;
        networkc.gridy = 2;
        networkc.gridwidth = 6;
        networkc.weightx = 1;

        panel.add(game, gamec);
        panel.add(graph, graphc);

        JPanel networkPanel = new JPanel(null);
        networkPanel.setPreferredSize(new Dimension(1000, 250));

        JScrollPane scrollPane = new JScrollPane(network);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 0, 1000, 250);


        networkPanel.add(scrollPane);

        panel.add(networkPanel, networkc);

        frame.add(panel);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}