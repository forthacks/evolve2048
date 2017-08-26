package com.github.forhacks.evolve2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GameGUI extends JPanel {

    Game game;

    public GameGUI(boolean allowKeypresses) {

        game = new Game();

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
                            initGame();
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

        initGame();

    }

    public void move(int direction) {
        game.move(direction);
        repaint();
    }

    public void initGame() {

        game.initGame();
        game.repaint();

    }

    @Override
    public void paint(Graphics g2) {
        super.paint(g2);

        Graphics2D g = (Graphics2D) g2;

        int[][] grid = game.getGrid();

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

        if (!game.game) {
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

}
