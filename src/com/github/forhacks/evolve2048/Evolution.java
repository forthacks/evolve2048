package com.github.forhacks.evolve2048;

import java.util.Arrays;
import java.util.Comparator;

public class Evolution {

    private static final int PLAYER_NUM = 1000;
    private static final int NUM_TRIAL = 5;
    private static final int GEN_NUM = 10;
    private static final double KILL_RATE = 0.5;

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
                score = 0;
                for (int k = 0; k < NUM_TRIAL; k++) {
                    Main.g.initGame();
                    score += run(players[j]);
                }
                scores[j] = score/NUM_TRIAL;
//                System.out.println(scores[j]);
            }
            System.out.println(i);
            Integer[] indices = new Integer[PLAYER_NUM];
            for (int j = 0; j < indices.length; j++) {
                indices[j] = j;
            }
            Arrays.sort(indices, Comparator.comparingInt((Integer o) -> (scores[o])));
            Player[] tempPlayers = players.clone();
            for (int j = 0; j < indices.length; j++) {
                players[j] = tempPlayers[indices[j]];
            }
            for (int j = 0; j < PLAYER_NUM * KILL_RATE; j++) {
                players[j + (int) (PLAYER_NUM * KILL_RATE)] = players[j].clone();
                players[j].mutate();
            }
        }

    }

    public int run(Player p) {
        for (;;) {
            if (!Main.g.game) {
                break;
            }
            Main.g.move(p.run(Main.g.grid));
        }
//        System.out.println(Main.g.score);
        return Main.g.score;
    }
}
