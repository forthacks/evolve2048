package com.github.forhacks.evolve2048;

public class Evolution {

    private static final int PLAYER_NUM = 1000;
    private static final int NUM_TRIAL = 1;
    private static final int GEN_NUM = 10;
    private static final double KILL_RATE = 0.5;

    Player[] players = new Player[4];
    public Evolution() {

        for (int i = 0; i < PLAYER_NUM; i++) {
            players[i] = Player.generate();
        }
        for (int i = 0; i < GEN_NUM; i++) {
            for (int j = 0; j < PLAYER_NUM; j++) {
                for (int k = 0; k < NUM_TRIAL; i++) {
//                    run();
                }
            }
        }

    }

    public int run(Player p) {
        while (Main.g.game) {
            Main.g.move(p.run(Main.g.grid));
        }
        return 0;
    }
}
