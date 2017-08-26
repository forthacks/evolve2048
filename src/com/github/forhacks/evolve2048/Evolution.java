package com.github.forhacks.evolve2048;

import java.util.Arrays;
import java.util.Comparator;

public class Evolution {

    private static final int PLAYER_NUM = 1000;
    private static final int NUM_TRIAL = 1;
    private static final int GEN_NUM = 10;
    private static final int KILL_RATE = 500;

    Player[] players = new Player[PLAYER_NUM];
    int[] scores = new int[PLAYER_NUM];

    int score = 0;

    public Evolution() {

        for (int i = 0; i < PLAYER_NUM; i++) {
            players[i] = Player.generate();
        }
        for (int i = 0; i < GEN_NUM; i++) {
            Arrays.fill(scores, 0);
            for (int j = 0; j < PLAYER_NUM; j++) {
                int score = 0;
                for (int k = 0; k < NUM_TRIAL; i++) {
                    run(players[j]);
                    score += Main.g.score;
                    Main.g.initGame();
                }
                scores[j] = score/NUM_TRIAL;
            }
            Integer[] indices = new Integer[PLAYER_NUM];
            for (int j = 0; j < indices.length; j++) {
                indices[j] = j;
            }
            Arrays.sort(indices, Comparator.comparingInt((Integer o) -> (scores[o])));
            Player[] tempPlayers = players.clone();
            for (int j = 0; j < indices.length; j++) {
                players[j] = tempPlayers[indices[j]];
            }
            for (int j = 0; j < KILL_RATE; j++) {
                players[j+KILL_RATE] = players[j].clone();
                players[j].mutate();
            }
        }

    }

    public int run(Player p) {
        while (Main.g.game) {
            Main.g.move(p.run(Main.g.grid));
        }
        score = Main.g.score;
        return 0;
    }
}
