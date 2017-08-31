package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Game extends JPanel {

    // grid[row][column]
    public int[][] grid;

    private Random random = new Random();

    public boolean game;

    public int score = 0;

    public Game() {

        initGame();

    }

    public void initGame() {

        grid = new int[4][4];

        addTile();
        addTile();

        score = 0;
        game = true;

    }

    private boolean canMove() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((i < 3 && grid[i][j] == grid[i+1][j]) || (j < 3 && grid[i][j] == grid[i][j+1])) {
                    return true;
                }
                if (grid[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMove(int direction) {

        if (direction == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if ((grid[i][j] == 0 && grid[i+1][j] != 0) || (grid[i][j] == grid[i+1][j])) {
                        return true;
                    }
                }
            }
        } else if (direction == 1) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((grid[i][j] == 0 && grid[i][j+1] != 0) || (grid[i][j] == grid[i][j+1])) {
                        return true;
                    }
                }
            }
        } else if (direction == 2) {
            for (int i = 3; i >= 1; i--) {
                for (int j = 0; j < 4; j++) {
                    if ((grid[i][j] == 0 && grid[i-1][j] != 0) || (grid[i][j] == grid[i-1][j])) {
                        return true;
                    }
                }
            }
        } else if (direction == 3) {
            for (int i = 0; i < 4; i++) {
                for (int j = 3; j >= 1; j--) {
                    if ((grid[i][j] == 0 && grid[i][j-1] != 0) || (grid[i][j] == grid[i][j-1])) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public int maxTile() {

        int max = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                max = Integer.max(grid[i][j], max);
            }
        }

        return max;

    }

    private void addTile() {

        List<int[]> available = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == 0) {
                    available.add(new int[] {i, j});
                }
            }
        }

        int[] add = available.get(random.nextInt(available.size()));

        int number = 2;

        float chance = random.nextFloat();

        if (chance <= 0.10f) {
            number = 4;
        }

        grid[add[0]][add[1]] = number;

    }


    public int[][] getGrid() {
        return grid;
    }

    // 0: up; 1: right; 2: down; 3: left
    public void move(int direction) {

        int tot = 0;

        if (direction == 0) {

            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++) {

                    int moves = 0;
                    while (j - moves - 1 >= 0 && grid[j - moves - 1][i] == 0) {
                        moves++;
                    }

                    int value = grid[j][i];
                    grid[j][i] = 0;
                    grid[j - moves][i] = value;

                    if (j - moves - 1 >= 0 && grid[j - moves - 1][i] == grid[j - moves][i]) {
                        grid[j - moves - 1][i] *= 2;
                        grid[j - moves][i] = 0;
                        score += grid[j - moves - 1][i];
                        moves++;
                    }

                    tot += moves;

                }
            }

        } else if (direction == 1) {

            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++) {

                    int moves = 0;
                    while (j - moves - 1 >= 0 && grid[i][j - moves - 1] == 0) {
                        moves++;
                    }

                    int value = grid[i][j];
                    grid[i][j] = 0;
                    grid[i][j - moves] = value;

                    if (j - moves - 1 >= 0 && grid[i][j - moves - 1] == grid[i][j - moves]) {
                        grid[i][j - moves - 1] *= 2;
                        grid[i][j - moves] = 0;
                        score += grid[j - moves - 1][i];
                        moves++;
                    }

                    tot += moves;

                }
            }

        } else if (direction == 2) {

            for (int i = 0; i < 4; i++) {
                for (int j = 2; j >= 0; j--) {

                    int moves = 0;
                    while (j + moves + 1 <= 3 && grid[j + moves + 1][i] == 0) {
                        moves++;
                    }

                    int value = grid[j][i];
                    grid[j][i] = 0;
                    grid[j + moves][i] = value;

                    if (j + moves + 1 <= 3 && grid[j + moves + 1][i] == grid[j + moves][i]) {
                        grid[j + moves + 1][i] *= 2;
                        grid[j + moves][i] = 0;
                        score += grid[j + moves + 1][i];
                        moves++;
                    }

                    tot += moves;

                }
            }

        } else if (direction == 3) {

            for (int i = 0; i < 4; i++) {
                for (int j = 2; j >= 0; j--) {

                    int moves = 0;
                    while (j + moves + 1 <= 3 && grid[i][j + moves + 1] == 0) {
                        moves++;
                    }

                    int value = grid[i][j];
                    grid[i][j] = 0;
                    grid[i][j + moves] = value;

                    if (j + moves + 1 <= 3 && grid[i][j + moves + 1] == grid[i][j + moves]) {
                        grid[i][j + moves + 1] *= 2;
                        grid[i][j + moves] = 0;
                        score += grid[j + moves + 1][i];
                        moves++;
                    }

                    tot += moves;

                }
            }

        }

        if (tot > 0) {
            addTile();
        }

        if (!canMove()) {
            game = false;
        }
    }

}
