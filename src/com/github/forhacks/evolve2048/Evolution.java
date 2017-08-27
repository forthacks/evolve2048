package com.github.forhacks.evolve2048;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.IntFunction;

public class Evolution {

    private static final int PLAYER_NUM = 5;
    private static final int NUM_TRIAL = 5;
    private static final int GEN_NUM = 10;
    private static final double KILL_RATE = 0.5;

    Player[] players = new Player[PLAYER_NUM];

    public Evolution() throws InterruptedException, ExecutionException {

        for (int i = 0; i < PLAYER_NUM; i++) {
            players[i] = Player.generate();
        }
        for (int i = 0; i < GEN_NUM; i++) {

            int[] scores = run();

            Integer[] indices = new Integer[PLAYER_NUM];

            for (int j = 0; j < indices.length; j++) {
                indices[j] = j;
            }

            Arrays.sort(indices, Comparator.comparingInt((Integer o) -> (-scores[o])));
            for (int z : indices)
                System.out.println(z);
            System.out.println();
            for (int z : scores) {
                System.out.println(z);
            }

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
                Main.g.move(players[0].run(Main.g.game));
                Thread.sleep(20);
            }
        }
    }

    public int[] run() throws InterruptedException, ExecutionException {

        List<Callable<Integer>> threads = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(PLAYER_NUM);

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
