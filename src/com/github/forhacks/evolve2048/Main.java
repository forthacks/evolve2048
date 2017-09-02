package com.github.forhacks.evolve2048;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main extends JPanel {

    static GamePanel game;
    static GraphPanel graph;
    static NetworkPanel network;
    static Evolution evolution;
    static Random random;
    private static long seed;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        random = new Random();
        seed = random.nextLong();
        random.setSeed(seed);

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
        one.ipadx = 10;
        one.gridwidth = 1;
        one.weightx = 1;

        GridBagConstraints two = new GridBagConstraints();
        two.fill = GridBagConstraints.HORIZONTAL;
        two.gridx = 1;
        two.gridy = 0;
        two.ipady = 20;
        two.gridwidth = 1;
        two.weightx = 1;

        GridBagConstraints three = new GridBagConstraints();
        three.fill = GridBagConstraints.HORIZONTAL;
        three.gridx = 2;
        three.gridy = 0;
        three.ipady = 20;
        three.gridwidth = 1;
        three.weightx = 1;

        GridBagConstraints four = new GridBagConstraints();
        four.fill = GridBagConstraints.HORIZONTAL;
        four.gridx = 3;
        four.gridy = 0;
        four.ipady = 20;
        four.gridwidth = 1;
        four.weightx = 1;

        JTextArea seedLabel = new JTextArea();
        seedLabel.setText(Long.toString(seed));
        seedLabel.setBackground(null);
        seedLabel.setMargin(new Insets(20, 10, 0, 0));

        Document doc = seedLabel.getDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    seed = Long.parseLong(seedLabel.getText());
                } catch (Exception err) {
                    System.out.println("Error!");
                }
                random.setSeed(seed);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    seed = Long.parseLong(seedLabel.getText());
                } catch (Exception err) {
                    System.out.println("Error!");
                }
                random.setSeed(seed);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    seed = Long.parseLong(seedLabel.getText());
                } catch (Exception err) {
                    System.out.println("Error!");
                }
                random.setSeed(seed);
            }
        });


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

        panel.add(seedLabel, one);
        panel.add(start, two);
        panel.add(pause, three);
        panel.add(run, four);

        GridBagConstraints gamec = new GridBagConstraints();
        gamec.fill = GridBagConstraints.HORIZONTAL;
        gamec.gridx = 0;
        gamec.gridy = 1;
        gamec.gridwidth = 2;
        gamec.weightx = 1;

        GridBagConstraints graphc = new GridBagConstraints();
        graphc.fill = GridBagConstraints.HORIZONTAL;
        graphc.gridx = 2;
        graphc.gridy = 1;
        graphc.gridwidth = 2;
        graphc.weightx = 1;

        GridBagConstraints networkc = new GridBagConstraints();
        networkc.fill = GridBagConstraints.HORIZONTAL;
        networkc.gridx = 0;
        networkc.gridy = 2;
        networkc.gridwidth = 4;
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