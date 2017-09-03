package com.github.forhacks.evolve2048;

import java.util.*;
import java.util.concurrent.*;

class Evolution {

    private static final int PLAYER_NUM = 80;
    private static final int NUM_TRIAL = 30;
    private static final double KILL_RATE = 0.5;

    List<Player> bestplayers = new ArrayList<>();

    private Player[] players = new Player[PLAYER_NUM];
    double maxscore = 0;
    List<Double> bests = new ArrayList<>();
    List<Double> medians = new ArrayList<>();

    private boolean running = false;

    Evolution() {
        for (int i = 0; i < PLAYER_NUM; i++) {
            players[i] = new Player();
        }
    }

    void start() throws InterruptedException, ExecutionException {

        if (running) return;

        new Thread(() -> {

            try {

                running = true;

                while (running) {

                    double[][] r = run();

                    Integer[] indices = new Integer[PLAYER_NUM];

                    for (int j = 0; j < indices.length; j++) {
                        indices[j] = j;
                    }

                    Arrays.sort(indices, Comparator.comparingDouble((Integer o) -> (-r[o][0])));

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

                    double median;

                    Arrays.sort(r, (double[] o1, double[] o2) -> Double.compare(o2[1], o1[1]));

                    if (r.length % 2 == 0)
                        median = (r[r.length / 2][0] + r[r.length / 2 - 1][0]) / 2;
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

    void pause() {
        running = false;
    }

    void runBest() {

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
                } catch (Exception e) {
                    System.out.println("Error!");
                }

            }


        }).start();

    }

    private double[][] run() throws InterruptedException, ExecutionException {

        List<Callable<double[]>> threads = new ArrayList<>();

        ExecutorService executor = Executors.newWorkStealingPool();

        for (int i = 0; i < PLAYER_NUM; i++) {

            final int z = i;

            Callable<double[]> thread = () -> {

                double score = 0;
                double max = 0;
                double empty = 0;
                double total = 0;

                List<Callable<double[]>> ts = new ArrayList<>();

                for (int j = 0; j < NUM_TRIAL; j++) {
                    Callable<double[]> t2 = () -> {
                        Game game = new Game();
                        double gempty = 0;
                        double gmoves = 0;
                        double gmax;
                        for (;;) {
                            gempty += game.emptyTileCount();
                            gmoves += 1;
                            if (!game.game) {
                                gmax = game.maxTile();
                                return new double[] {game.score, gmax, gempty, gmoves};
                            }
                            int r = players[z].run(game);
                            game.move(r);
                        }
                    };
                    ts.add(t2);
                }

                List<Future<double[]>> s2 = executor.invokeAll(ts);

                for (int j = 0; j < NUM_TRIAL; j++) {
                    double[] b = s2.get(j).get();
                    score += b[0];
                    max += b[1];
                    empty += b[2];
                    total += b[3];
                }

                double ratio = empty / total / 16;

//                System.out.println("score = " + score);
//                System.out.println("max = " + max);
//                System.out.println("empty = " + empty);
//                System.out.println("total = " + total);
//                System.out.println("ratio = " + ratio);
//                System.out.println("total = " + total);
//                System.out.println();

                double fitness = ratio * 1000 + score + max;

                return new double[] {score / NUM_TRIAL, fitness};
                // [score, fitness]

            };

            threads.add(thread);

        }

        List<Future<double[]>> scores = executor.invokeAll(threads);

        double[][] result = new double[PLAYER_NUM][2];

        for (int i = 0; i < PLAYER_NUM; i++) {
            double[] a = scores.get(i).get();
            result[i] = a;
        }

        return result;

    }
}
