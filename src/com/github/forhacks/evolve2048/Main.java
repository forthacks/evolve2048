package com.github.forhacks.evolve2048;

import javax.swing.*;

public class Main {

    private static Game game;

    public static void main(String[] args) {

        JFrame game = new JFrame();
        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(500, 520);
        game.setResizable(false);

        game.add(new Game());

        game.setLocationRelativeTo(null);
        game.setVisible(true);

    }

}
