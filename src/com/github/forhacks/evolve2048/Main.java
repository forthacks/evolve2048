package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class Main {

    public static GameGUI g;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        JFrame game = new JFrame();
        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setUndecorated(true);
        game.setSize(1000, 500);
        game.setResizable(false);

        g = new GameGUI(false);

        game.add(g);

        game.setLocationRelativeTo(null);
        game.setVisible(true);
        new Evolution();
    }

}
