package com.github.forhacks.evolve2048;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Evolution {

    private static final int PLAYER_NUM = 6;
    private static final int NUM_TRIAL = 2;
    private static final int GEN_NUM = 20000;
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

                    int[] scores = run();

                    Integer[] indices = new Integer[PLAYER_NUM];

                    for (int j = 0; j < indices.length; j++) {
                        indices[j] = j;
                    }

                    Arrays.sort(indices, Comparator.comparingInt((Integer o) -> (-scores[o])));

                    Player[] tempPlayers = players.clone();
                    for (int j = 0; j < indices.length; j++) {
                        players[j] = tempPlayers[indices[j]];
                    }

                    Arrays.sort(scores);

                    int median;

                    if (scores.length % 2 == 0)
                        median = (int) ((double) scores[scores.length / 2] + (double) scores[scores.length / 2 - 1]) / 2;
                    else
                        median = scores[scores.length / 2];

                    this.medians.add(median);
                    if (median > maxscore)
                        maxscore = median;

                    int best = IntStream.of(scores).max().getAsInt();

                    this.bests.add(best);
                    if (best > maxscore)
                        maxscore = best;

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

        Main.network.repaint();

        new Thread(() -> {

            Main.game.initGame();

            for (;;) {

                if (!Main.game.game.game) {
                    break;
                }

                Main.game.move(player.run(Main.game.game));

                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    System.out.println("Error!");
                }

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

    public int[] run() throws InterruptedException, ExecutionException {

        List<Callable<Integer>> threads = new ArrayList<>();

        ExecutorService executor = Executors.newWorkStealingPool();

        for (int i = 0; i < PLAYER_NUM; i++) {

            final int z = i;

            Callable<Integer> thread = () -> {

                int score = 0;
                for (int j = 0; j < NUM_TRIAL; j++) {
                    Game game = new Game();
                    for (;;) {
                        if (!game.game) {
                            break;
                        }
                        int r = players[z].run(game);
                        game.move(r);
                    }
                    score += game.score;
                }
                return score / NUM_TRIAL;

            };

            threads.add(thread);

        }

        List<Future<Integer>> scores = executor.invokeAll(threads);

        int[] result = new int[PLAYER_NUM];

        for (int i = 0; i < PLAYER_NUM; i++) {
            result[i] = scores.get(i).get();
        }

        return result;

    }
}
