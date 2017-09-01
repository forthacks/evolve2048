package com.github.forhacks.evolve2048;

import java.util.*;
import java.util.concurrent.*;

public class Evolution {

    private static final int PLAYER_NUM = 80;
    private static final int NUM_TRIAL = 30;
    private static final double KILL_RATE = 0.5;

    List<Player> bestplayers = new ArrayList<>();

    Player[] players = new Player[PLAYER_NUM];
    int maxscore = 0;
    List<Integer> bests = new ArrayList<>();
    List<Integer> medians = new ArrayList<>();

    boolean running = false;

    public Evolution() {
        for (int i = 0; i < PLAYER_NUM; i++) {
            players[i] = new Player();
        }
    }

    public void start() throws InterruptedException, ExecutionException {

        new Thread(() -> {

            try {

                running = true;

                while (running) {

                    Integer[][] r = run();

                    Integer[] indices = new Integer[PLAYER_NUM];

                    for (int j = 0; j < indices.length; j++) {
                        indices[j] = j;
                    }

                    Arrays.sort(indices, Comparator.comparingInt((Integer o) -> (-r[o][0])));

                    Player[] tempPlayers = players.clone();
                    for (int j = 0; j < indices.length; j++) {
                        players[j] = tempPlayers[indices[j]];
                    }

                    int best = 0;
                    for (int i = 0; i < PLAYER_NUM; i++) {
                        if (r[i][1] > r[best][1]) {
                            best = i;
                        }
                    }

                    this.bests.add(r[best][0]);
                    if (r[best][0] > maxscore)
                        maxscore = r[best][0];

                    int median;

                    Arrays.sort(r, (Integer[] o1, Integer[] o2) -> o2[1].compareTo(o1[1]));

                    if (r.length % 2 == 0)
                        median = (int) ((double) r[r.length / 2][0] + (double) r[r.length / 2 - 1][0]) / 2;
                    else
                        median = r[r.length / 2][0];

                    this.medians.add(median);
                    if (median > maxscore)
                        maxscore = median;

                    Main.graph.repaint();

                    for (int j = 0; j < PLAYER_NUM * (1 - KILL_RATE); j++) {
                        players[j + (int) (PLAYER_NUM * (1 - KILL_RATE))] = new Player(players[j]);
                        players[j].mutate();
                    }

                }

            } catch (Exception e) {
                System.out.println("Error!");
            }

        }).start();

    }

    public void pause() {
        running = false;
    }

    public void runBest() {

        Player player = new Player(players[0]);

        bestplayers.add(player);

        new Thread(() -> {

            Main.game.initGame();

            for (;;) {

                if (!Main.game.game.game) {
                    break;
                }

                Main.game.move(player.run(Main.game.game));
                Main.network.repaint();

                try {
                    Thread.sleep(10);
                } catch (Exception e) {}

            }


        }).start();

    }

    public boolean isRipple() {
        int[][] grid = Main.game.game.grid;
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
        return false;
    }

    public Integer[][] run() throws InterruptedException, ExecutionException {

        List<Callable<Integer[]>> threads = new ArrayList<>();

        ExecutorService executor = Executors.newWorkStealingPool();

        for (int i = 0; i < PLAYER_NUM; i++) {

            final int z = i;

            Callable<Integer[]> thread = () -> {

                int score = 0;
                int max = 0;
                for (int j = 0; j < NUM_TRIAL; j++) {
                    Game game = new Game();
                    for (;;) {
                        if (!game.game) {
                            max += game.maxTile();
                            break;
                        }
                        int r = players[z].run(game);
                        game.move(r);
                    }
                    score += game.score;
                }
                return new Integer[] {score / NUM_TRIAL, score / NUM_TRIAL};
                // [score, fitness]

            };

            threads.add(thread);

        }

        List<Future<Integer[]>> scores = executor.invokeAll(threads);

        Integer[][] result = new Integer[PLAYER_NUM][2];

        for (int i = 0; i < PLAYER_NUM; i++) {
            Integer[] a = scores.get(i).get();
            result[i] = a;
        }

        return result;

    }
}
