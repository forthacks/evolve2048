package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Game extends JPanel {

    // grid[row][column]
    private int[][] grid;

    private Random random = new Random();

    private boolean game;

    public Game(boolean allowKeypresses) {

        setPreferredSize(new Dimension(500, 520));
        setFocusable(true);
        setOpaque(true);
        setBackground(new Color(0xbbada0));

        if (allowKeypresses) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ESCAPE:
                            grid = new int[4][4];
                            addTile();
                            addTile();
                            game = true;
                            repaint();
                            break;
                        case KeyEvent.VK_UP:
                            move(0);
                            repaint();
                            break;
                        case KeyEvent.VK_LEFT:
                            move(1);
                            repaint();
                            break;
                        case KeyEvent.VK_DOWN:
                            move(2);
                            repaint();
                            break;
                        case KeyEvent.VK_RIGHT:
                            move(3);
                            repaint();
                            break;
                    }
                }
            });
        }

        grid = new int[4][4];

        addTile();
        addTile();

        game = true;

    }

    private boolean canMove() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((i < 3 && grid[i][j] == grid[i+1][j]) || ((j < 3) && grid[i][j] == grid[i][j+1])) {
                    return true;
                }
                if (grid[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
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

                g.setColor(getBackground(grid[i][j]));
                g.fillRoundRect(20 + 120 * j, 20 + 120 * i, 100, 100, 15, 15);


                if (grid[i][j] != 0) {
                    String s = Integer.toString(grid[i][j]);
                    g.setColor(Color.BLACK);
                    g.drawString(s, 70 + 120 * j - fm.stringWidth(s) / 2, 120 * (i + 1) - (100 + fm.getLineMetrics(s, g).getBaselineOffsets()[2]) / 2 - 2);
                }

            }
        }

        if (!game) {
            g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
            g.fillRect(0, 0, 500, 500);
        }
    }

    private Color getBackground(int value) {
        switch (value) {
            case 2:    return new Color(0xeee4da);
            case 4:    return new Color(0xede0c8);
            case 8:    return new Color(0xf2b179);
            case 16:   return new Color(0xf59563);
            case 32:   return new Color(0xf67c5f);
            case 64:   return new Color(0xf65e3b);
            case 128:  return new Color(0xedcf72);
            case 256:  return new Color(0xedcc61);
            case 512:  return new Color(0xedc850);
            case 1024: return new Color(0xedc53f);
            case 2048: return new Color(0xedc22e);
        }
        return new Color(0xcdc1b4);
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
            addTile();
        }

        if (!canMove()) {
            game = false;
        }

    }

}
