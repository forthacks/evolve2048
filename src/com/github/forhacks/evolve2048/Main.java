package com.github.forhacks.evolve2048;

import javax.swing.*;

public class Main {

    public static GameGUI g;

    public static void main(String[] args) throws InterruptedException {

        JFrame game = new JFrame();
        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(500, 520);
        game.setResizable(false);

        g = new GameGUI(false);

        game.add(g);

        game.setLocationRelativeTo(null);
        game.setVisible(true);
        new Evolution();
    }

}
