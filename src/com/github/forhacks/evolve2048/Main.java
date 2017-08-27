package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class Main extends JPanel {

    public static GameGUI game;
    public static GraphGUI graph;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        JFrame frame = new JFrame();
        frame.setTitle("2048");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setSize(1000, 500);
        frame.setResizable(false);

        game = new GameGUI(false);
        graph = new GraphGUI(game);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(game);
        panel.add(graph);

        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new Evolution();
    }

}
