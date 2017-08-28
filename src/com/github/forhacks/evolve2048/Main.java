package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class Main extends JPanel {

    public static GamePanel game;
    public static GraphPanel graph;
    public static Evolution evolution;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        JFrame frame = new JFrame();
        frame.setTitle("2048");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setSize(1000, 550);
        frame.setResizable(false);

        game = new GamePanel(false);

        graph = new GraphPanel();

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gamec = new GridBagConstraints();

        panel.add(game);
        panel.add(graph);

        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        evolution = new Evolution();
        evolution.start();

    }

}
