package com.github.forhacks.evolve2048;

import java.util.Arrays;
import java.util.Comparator;

public class Evolution {

    private static final int PLAYER_NUM = 15;
    private static final int NUM_TRIAL = 5;
    private static final int GEN_NUM = 10;
    private static final double KILL_RATE = 0.5;

    Player[] players = new Player[PLAYER_NUM];
    int[] scores = new int[PLAYER_NUM];

    int score = 0;

    public Evolution() throws InterruptedException {

        for (int i = 0; i < PLAYER_NUM; i++) {
            players[i] = Player.generate();
        }
        for (int i = 0; i < GEN_NUM; i++) {
            int max=0;
            Arrays.fill(scores, 0);
            for (int j = 0; j < PLAYER_NUM; j++) {
                score = 0;
                int count = 0;
                for (int k = 0; k < NUM_TRIAL; k++) {
                    Main.g.initGame();
                    run(players[j]);
                    score += Main.g.game.score;
                    if (isRipple()) {
                        count++;
                    }
                }
                if (count >= NUM_TRIAL-1) {
                    score = 0;
                }
                scores[j] = score/NUM_TRIAL;
//                System.out.println(scores[j]);
                System.out.print(scores[j] + " ");
                max = Math.max(max, scores[j]);
            }
            System.out.println();
            System.out.println(max);
            System.out.println(i);
            Integer[] indices = new Integer[PLAYER_NUM];
            for (int j = 0; j < indices.length; j++) {
                indices[j] = j;
            }
            Arrays.sort(indices, Comparator.comparingInt((Integer o) -> (-scores[o])));
            Player[] tempPlayers = players.clone();
            for (int j = 0; j < indices.length; j++) {
                players[j] = tempPlayers[indices[j]];
            }
            for (int j = 0; j < PLAYER_NUM * KILL_RATE; j++) {
                players[j + (int) (PLAYER_NUM * KILL_RATE)] = players[j].clone();
                players[j].mutate();
            }
            Main.g.initGame();
            for (;;) {
                if (!Main.g.game.game) {
                    break;
                }
                Main.g.move(players[0].run(Main.g.game.grid));
                Thread.sleep(100);
            }
        }
    }
    public int fitness(){
        return 0;
    }

    public boolean isRipple() {
        int[][] grid = Main.g.game.grid;
        if ((grid[0][2] == grid[1][1]) && (grid[0][2] == grid[2][0])) {
            return true;
        } else if ((grid[1][3] == grid[2][2]) && (grid[1][3] == grid[3][1])) {
            return true;
        } else if (((grid[0][3] == grid[1][2]) && (grid[0][3] == grid[2][1])) ||
                ((grid[0][3] == grid[1][2]) && (grid[0][3] == grid[3][0])) ||
                ((grid[0][3] == grid[2][1]) && (grid[0][3] == grid[3][0])) ||
                ((grid[1][2] == grid[2][1]) && (grid[1][2] == grid[3][0]))) {
            return true;
        }
//        if ((grid[0][1] == grid[1][2]) && (grid[0][1] == grid[2][3])) {
//            return true;
//        } else if ((grid[1][0] == grid[2][1]) && (grid[1][0] == grid[3][2])) {
//            return true;
//        } else if (((grid[0][0] == grid[1][1]) && (grid[0][0] == grid[2][2])) ||
//                ((grid[0][0] == grid[1][1]) && (grid[0][0] == grid[3][3])) ||
//                ((grid[0][0] == grid[2][2]) && (grid[0][0] == grid[3][3])) ||
//                ((grid[1][1] == grid[2][2]) && (grid[1][1] == grid[3][3]))) {
//            return true;
//        }
        return false;
    }
    public void run(Player p) {
        for (;;) {
            if (!Main.g.game.game) {
                break;
            }
            Main.g.move(p.run(Main.g.game.grid));
        }
//        System.out.println(Main.g.score);
    }
}
