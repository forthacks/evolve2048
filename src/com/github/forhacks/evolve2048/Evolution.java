package com.github.forhacks.evolve2048;

import sun.jvm.hotspot.runtime.Threads;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Evolution {

    private static final int PLAYER_NUM = 2;
    private static final int NUM_TRIAL = 1;
    private static final int GEN_NUM = 20;
    private static final double KILL_RATE = 0.5;

    Player[] players = new Player[PLAYER_NUM];
    int maxscore = 0;
    List<Integer> scores = new ArrayList<>();

    public void start() throws InterruptedException, ExecutionException {

        Runnable runBest = () -> {

            try {

                Player player;
                try {
                    player = players[0].clone();
                } catch (ConcurrentModificationException e) {
                    player = players[0].clone();
                }

                Main.game.initGame();

                for (;;) {
                    if (!Main.game.game.game) {
                        break;
                    }
                    Main.game.move(player.run(Main.game.game));
                    Thread.sleep(20);
                }

            } catch (Exception e) {
                Thread t = Thread.currentThread();
                t.getUncaughtExceptionHandler().uncaughtException(t, e);
            }

        };

        Runnable calc = () -> {

            try {

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

                int max = IntStream.of(scores).max().getAsInt();
                if (max > maxscore)
                    maxscore = max;
                this.scores.add(max);
                Main.graph.repaint();

                for (int j = 0; j < PLAYER_NUM * KILL_RATE; j++) {
                    players[j + (int) (PLAYER_NUM * KILL_RATE)] = players[j].clone();
                    players[j].mutate();
                }

            } catch (Exception e) {
                Thread t = Thread.currentThread();
                t.getUncaughtExceptionHandler().uncaughtException(t, e);
            }

        };

        for (int i = 0; i < PLAYER_NUM; i++) {
            players[i] = Player.generate();
        }

        Thread t = new Thread(calc);
        t.start();
        t.join();

        for (int i = 0; i < GEN_NUM; i++) {

            Thread t1 = new Thread(runBest);
            Thread t2 = new Thread(calc);
            t1.start();
            t2.start();

            t1.join();
            t2.join();

        }

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
