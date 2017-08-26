package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {

    // grid[row][column]
    int[][] grid;

    public Game() {

        setPreferredSize(new Dimension(500, 520));
        setFocusable(true);

        grid = new int[4][4];

    }

    @Override
    public void paint(Graphics g2) {
        super.paint(g2);

        Graphics2D g = (Graphics2D) g2;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                g.setFont(new Font("Arial", Font.BOLD, grid[i][j] < 100 ? 36 : grid[j][i] < 1000 ? 32 : 24));

                FontMetrics fm = getFontMetrics(g.getFont());

                g.setColor(Color.LIGHT_GRAY);
                g.fillRoundRect(20 + 120 * i, 20 + 120 * j, 100, 100, 15, 15);

                String s = Integer.toString(grid[i][j]);

                g.setColor(Color.BLACK);
                g.drawString(s, 70 + 120 * i - fm.stringWidth(s) / 2, 120 * (j + 1) - (100 + fm.getLineMetrics(s, g).getBaselineOffsets()[2]) / 2 - 2);

            }
        }
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
                    }

                    tot += moves;

                }
            }

        }

        if (tot > 0) {
            repaint();
        }

    }

}
